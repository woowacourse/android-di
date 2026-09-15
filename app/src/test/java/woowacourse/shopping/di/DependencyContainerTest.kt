package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DependencyContainerTest {
    @Test
    fun `ViewModel을 자동 생성하고 Repository를 공유한다`() {
        val productsViewModel = DependencyContainer.create(ProductsViewModel::class.java)
        val cartViewModel = DependencyContainer.create(CartViewModel::class.java)
        val product = Product(name = "우테코 과자", price = 10_000, imageUrl = "")

        productsViewModel.addCartProduct(product)
        cartViewModel.getAllCartProducts()

        assertThat(cartViewModel.uiState.value.cartProducts).containsExactly(product)
    }
}
