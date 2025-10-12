package woowacourse.shopping.di

import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class DependencyInjectorTest {
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mockk<CartRepository>()
        DependencyInjector.setInstance(CartRepository::class, cartRepository)
    }

    @Test
    fun `인스턴스 생성 시 필드가 주입된다`() {
        // given

        // when
        val viewModel = DependencyInjector.getInstance(CartViewModel::class)
        DependencyInjector.injectAnnotatedProperties(CartViewModel::class, viewModel)

        // then
        val field = viewModel::class.java.getDeclaredField("cartRepository")
        field.isAccessible = true
        val actual = field.get(viewModel)
        Assert.assertEquals(cartRepository, actual)
    }
}
