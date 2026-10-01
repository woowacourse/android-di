package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.di.Injector
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class ViewModelInjectionTest {
    private val injector = Injector()

    @Test
    fun `두 ViewModel의 필드에 동일한 Repository를 주입한다`() {
        injector.registerSingleton(CartRepository::class, RoomCart::class) { FakeCartRepository() }
        val products = injector.create(ProductsViewModel::class)
        val cart = injector.create(CartViewModel::class)

        assertThat(products.cartRepository).isSameAs(cart.cartRepository)
        products.getAllProducts()
        assertThat(products.uiState.value.products).hasSize(3)
        assertThat(cart.uiState.value.cartProducts).isEmpty()
    }
}
