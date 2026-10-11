package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.di.DependencyScope
import woowacourse.di.dependencyContainer
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AutoViewModelFactory
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.domain.repository.CartRepository

class ViewModelScopeLifecycleTest {
    @Test
    fun `ProductRepository는 ViewModel마다 생성되고 ViewModel 정리 때 스코프가 닫힌다`() {
        val cartRepository = FakeCartRepository()
        val container =
            dependencyContainer {
                registerInstance(CartRepository::class, cartRepository, RoomCart::class)
                registerFactory(ProductRepository::class, scope = ShoppingScopes.VIEW_MODEL) {
                    ProductRepository()
                }
            }
        val scopes = mutableListOf<DependencyScope>()
        val factory =
            AutoViewModelFactory(container) {
                container.openScope(ShoppingScopes.VIEW_MODEL).also(scopes::add)
            }
        val firstStore = ViewModelStore()
        val secondStore = ViewModelStore()
        val firstProvider = ViewModelProvider(firstStore, factory)

        val firstViewModel = firstProvider[ProductScopeTestViewModel::class.java]
        val retainedViewModel = firstProvider[ProductScopeTestViewModel::class.java]
        val secondViewModel = ViewModelProvider(secondStore, factory)[ProductScopeTestViewModel::class.java]

        assertThat(retainedViewModel).isSameInstanceAs(firstViewModel)
        assertThat(firstViewModel.productRepository).isNotSameInstanceAs(secondViewModel.productRepository)
        assertThat(firstViewModel.cartRepository).isSameInstanceAs(cartRepository)
        assertThat(secondViewModel.cartRepository).isSameInstanceAs(cartRepository)
        assertThat(scopes[0].isClosed).isFalse()

        firstStore.clear()

        assertThat(scopes[0].isClosed).isTrue()
        assertThat(scopes[1].isClosed).isFalse()
        secondStore.clear()
    }
}

class ProductScopeTestViewModel(
    val productRepository: ProductRepository,
    @param:RoomCart val cartRepository: CartRepository,
) : ViewModel()
