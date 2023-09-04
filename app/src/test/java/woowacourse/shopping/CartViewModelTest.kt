package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: DefaultCartRepository

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니에 담겨있는 모든 상품 목록을 불러온다`() {
        // given
        every {
            cartRepository.getAllCartProducts()
        } returns ProductFixture.products

        // when
        viewModel.getAllCartProducts()

        val actual = viewModel.cartProducts.value

        // then
        val expected = ProductFixture.products

        assertEquals(expected, actual)
    }

    @Test
    fun `장바구니에 담겨있는 상품 하나를 삭제하면 장바구니 상품이 삭제된 상태가 된다`() {
        // given
        val id = 0

        every {
            cartRepository.getAllCartProducts()
        } returns ProductFixture.products

        viewModel.getAllCartProducts()

        // when
        viewModel.deleteCartProduct(id)

        val actual = viewModel.onCartProductDeleted.value

        // then
        val expected = true

        assertEquals(expected, actual)
    }
}
