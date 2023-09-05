package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultFakeCartRepository
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    private lateinit var viewModel: CartViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupCartViewModel() {
        val cartRepository: CartRepository = DefaultFakeCartRepository()

        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 정보들을 가져온다`() {
        // given

        // when
        viewModel.getAllCartProducts()

        // then
        viewModel.cartProducts.getOrAwaitValue()

        val expectedCarts = CartFixture.carts
        assertEquals(expectedCarts, viewModel.cartProducts.value)
    }

    @Test
    fun `장바구니 상품 하나를 삭제한다`() {
        // given

        // when
        viewModel.deleteCartProduct(0)

        // then
        viewModel.onCartProductDeleted.getOrAwaitValue()

        val expectedIsDeleted = true
        assertEquals(expectedIsDeleted, viewModel.onCartProductDeleted.value)
    }
}
