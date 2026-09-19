package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DIViewModelFactoryTest {
    @Test
    fun `두 ViewModel이 같은 장바구니를 사용한다`() {
        val factory =
            DependencyViewModelFactory(
                mapOf(
                    ProductRepository::class to ProductRepository(),
                    CartRepository::class to CartRepository(),
                ),
            )
        val productsViewModel = factory.create(ProductsViewModel::class.java)
        val cartViewModel = factory.create(CartViewModel::class.java)
        val product =
            productsViewModel.run {
                getAllProducts()
                uiState.value.products.first()
            }

        productsViewModel.addCartProduct(product)
        cartViewModel.getAllCartProducts()

        assertThat(cartViewModel.uiState.value.cartProducts).containsExactly(product)
    }
}
