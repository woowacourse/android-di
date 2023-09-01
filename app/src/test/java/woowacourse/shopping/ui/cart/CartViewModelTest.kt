package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupCartViewModel() {
        cartRepository = mockk()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 정보들을 가져온다`() {
        // given
        every {
            cartRepository.getAllCartProducts()
        } answers {
            CartFixture.carts
        }

        // when
        viewModel.getAllCartProducts()

        // then
        viewModel.cartProducts.getOrAwaitValue()

        val expectedCarts = CartFixture.carts
        assertEquals(expectedCarts, CartFixture.carts)
    }

    @Test
    fun `장바구니 상품 하나를 삭제한다`() {
        // given
        every {
            cartRepository.deleteCartProduct(0)
        } just runs

        // when
        viewModel.deleteCartProduct(0)

        // then
        viewModel.onCartProductDeleted.getOrAwaitValue()

        val expectedIsDeleted = true
        assertEquals(expectedIsDeleted, viewModel.onCartProductDeleted.value)
    }
}
