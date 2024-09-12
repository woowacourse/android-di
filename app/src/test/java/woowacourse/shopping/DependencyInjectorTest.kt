package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.product.MainViewModel

class DependencyInjectorTest {
    private val cartProductDao = mockk<CartProductDao>()

    @Before
    fun setUp() {
        DependencyInjector.initialize(cartProductDao)
    }

    @Test
    fun `ProductRepository 주입 테스트`() {
        val productRepository = DependencyInjector.findInstance(ProductRepository::class)
        assertThat(productRepository).isNotNull()
    }

    @Test
    fun `CartRepository 주입 테스트`() {
        val cartRepository = DependencyInjector.findInstance(CartRepository::class)
        assertThat(cartRepository).isNotNull()
    }

    @Test
    fun `MainViewModel 주입 테스트`() {
        val mainViewModel = DependencyInjector.findInstance(MainViewModel::class)
        assertThat(mainViewModel).isNotNull()
    }

    @Test
    fun `CartViewModel 주입 테스트`() {
        val cartViewModel = DependencyInjector.findInstance(CartViewModel::class)
        assertThat(cartViewModel).isNotNull()
    }
}
