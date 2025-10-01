package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.TEST_PRODUCTS
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setUp() {
        cartRepository = mockk<CartRepository>()
        cartViewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `모든 장바구니 상품을 조회할 수 있다`() {
        // given
        every { cartRepository.getAllCartProducts() } returns TEST_PRODUCTS

        // when
        cartViewModel.getAllCartProducts()

        // then
        verify { cartRepository.getAllCartProducts() }
        Assert.assertEquals(
            TEST_PRODUCTS,
            cartViewModel.cartProducts.getOrAwaitValue(),
        )
    }

    @Test
    fun `상품을 제거할 수 있다`() {
        // given
        every { cartRepository.deleteCartProduct(any()) } returns Unit

        // when
        cartViewModel.deleteCartProduct(0)

        // then
        verify { cartRepository.deleteCartProduct(any()) }
        Assert.assertTrue(cartViewModel.onCartProductDeleted.getOrAwaitValue())
    }
}
