package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DiIntegrationTest {
    @Test
    fun `Application이 실제 qualifier 바인딩으로 ViewModel을 조립한다`() {
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .setup()
                .get()
        val application = activity.application as ShoppingApplication

        val productsViewModel = application.viewModelFactory.create(ProductsViewModel::class.java)
        val cartViewModel = application.viewModelFactory.create(CartViewModel::class.java)

        assertThat(productsViewModel.productRepository).isInstanceOf(ProductRepository::class.java)
        assertThat(productsViewModel.cartRepository).isInstanceOf(DefaultCartRepository::class.java)
        assertThat(cartViewModel.cartRepository).isSameInstanceAs(productsViewModel.cartRepository)
    }
}
