package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DIViewModelFactoryTest {
    @Test
    fun `두 ViewModel이 같은 장바구니를 사용한다`() {
        val productsViewModel = DIViewModelFactory.create(ProductsViewModel::class.java)
        val cartViewModel = DIViewModelFactory.create(CartViewModel::class.java)
        val product =
            productsViewModel.run {
                getAllProducts()
                uiState.value.products.first()
            }

        productsViewModel.addCartProduct(product)
        cartViewModel.getAllCartProducts()

        assertThat(cartViewModel.uiState.value.cartProducts).contains(product)
    }
}
