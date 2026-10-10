package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyScope
import woowacourse.di.KirbyDIContainer
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class KirbyViewModelFactoryTest {
    @Test
    fun `개별 ViewModel은 상품 저장소를 분리하고 장바구니 저장소는 공유한다`() {
        val container = applicationContainer()
        val store = ViewModelStore()
        val provider = ViewModelProvider(store, KirbyViewModelFactory(container))

        val first = provider["first", ProductsViewModel::class.java]
        val second = provider["second", ProductsViewModel::class.java]
        val cart = provider[CartViewModel::class.java]

        assertThat(provider["first", ProductsViewModel::class.java]).isSameAs(first)
        assertThat(scopeOf(first)).isNotSameAs(scopeOf(second))
        assertThat(first.dependency("productRepository")).isNotSameAs(second.dependency("productRepository"))
        assertThat(first.dependency("cartRepository")).isSameAs(second.dependency("cartRepository"))
        assertThat(first.dependency("cartRepository")).isSameAs(cart.dependency("cartRepository"))
        store.clear()
    }

    @Test
    fun `ViewModel은 정리될 때 자신의 스코프를 종료한다`() {
        val container = applicationContainer()
        val store = ViewModelStore()
        val provider = ViewModelProvider(store, KirbyViewModelFactory(container))
        val first = provider[ProductsViewModel::class.java]
        val oldScope = scopeOf(first)
        val oldProductRepository = first.dependency("productRepository")

        val otherStore = ViewModelStore()
        val other = ViewModelProvider(otherStore, KirbyViewModelFactory(container))[ProductsViewModel::class.java]
        val otherScope = scopeOf(other)
        val otherRepository = other.dependency("productRepository")

        store.clear()

        assertThatThrownBy { container.resolve(ProductRepository::class, scope = oldScope) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("종료된 스코프")
        assertThat(container.resolve(ProductRepository::class, scope = otherScope)).isSameAs(otherRepository)
        val next = provider[ProductsViewModel::class.java]
        assertThat(next).isNotSameAs(first)
        assertThat(next.dependency("productRepository")).isNotSameAs(oldProductRepository)
        assertThat(next.dependency("cartRepository")).isSameAs(first.dependency("cartRepository"))
        store.clear()
        otherStore.clear()
    }

    private fun applicationContainer(): KirbyDIContainer {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        return (activity.application as ShoppingApplication).container
    }

    private fun scopeOf(viewModel: ViewModel): DependencyScope =
        requireNotNull(viewModel.getCloseable<DependencyScope>(KirbyViewModelFactory.SCOPE_KEY))

    private fun ViewModel.dependency(name: String): Any {
        val field = javaClass.getDeclaredField(name)
        field.isAccessible = true
        return requireNotNull(field.get(this))
    }
}
