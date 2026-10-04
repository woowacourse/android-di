@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartProductDao
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.qualifier.Room
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeTest {
    class FakeRepository : CartRepository {
        override suspend fun addCartProduct(product: Product) {}

        override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

        override suspend fun deleteCartProduct(id: Long) {}
    }

    @Test
    fun `CartRepository는 앱 스코프 인스턴스를 다른 생명주기에서도 공유한다`() {
        val applicationRepository =
            DependencyContainer.getInstance(
                CartRepository::class,
                Room::class,
            )
        val viewModelScope = DependencyContainer.openScope("cart-repository-test")

        val scopedRepository =
            DependencyContainer.getInstance(
                CartRepository::class,
                Room::class,
                viewModelScope,
            )

        assertSame(applicationRepository, scopedRepository)

        viewModelScope.close()
    }

    @Test
    fun `ViewModel마다 별도 스코프를 사용하고 ViewModelStore가 비워지면 스코프가 종료된다`() {
        DependencyContainer.register(CartProductDao::class, FakeCartProductDao())
        DependencyContainer.register(CartRepository::class, Room::class, FakeRepository())

        val firstStore = ViewModelStore()
        val secondStore = ViewModelStore()
        val factory = AutoViewModelFactory()
        val firstViewModel = ViewModelProvider(firstStore, factory)[ProductsViewModel::class.java]
        val secondViewModel = ViewModelProvider(secondStore, factory)[ProductsViewModel::class.java]
        val firstScope = firstViewModel.dependencyScope!!
        val secondScope = secondViewModel.dependencyScope!!

        val firstRepository = DependencyContainer.getInstance(ProductRepository::class, scope = firstScope)
        val repeatedRepository = DependencyContainer.getInstance(ProductRepository::class, scope = firstScope)
        val secondRepository = DependencyContainer.getInstance(ProductRepository::class, scope = secondScope)

        assertSame(firstRepository, repeatedRepository)
        assertNotSame(firstRepository, secondRepository)

        firstStore.clear()
        assertTrue(firstScope.isClosed)
        assertFalse(secondScope.isClosed)
        secondStore.clear()
        assertTrue(secondScope.isClosed)
    }

    @Test
    fun `같은 ViewModelStore에서 화면 의존성을 재사용하고 제거 시 스코프를 닫는다`() {
        DependencyContainer.register(android.content.Context::class, RuntimeEnvironment.getApplication())
        val store = ViewModelStore()
        val provider = ViewModelProvider(store, AutoViewModelFactory())

        val first = provider[CartScreenScopeViewModel::class.java]
        val repeated = provider[CartScreenScopeViewModel::class.java]
        val scope = first.dependencyScope!!

        assertSame(first, repeated)
        assertSame(first.dateFormatter, repeated.dateFormatter)

        store.clear()

        assertTrue(scope.isClosed)

        val nextStore = ViewModelStore()
        val next = ViewModelProvider(nextStore, AutoViewModelFactory())[CartScreenScopeViewModel::class.java]

        assertNotSame(first.dateFormatter, next.dateFormatter)

        nextStore.clear()
    }
}
