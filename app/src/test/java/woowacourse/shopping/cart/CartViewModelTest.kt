package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 상품들을 불러온다`() {
        // given & when
        viewModel.getAllCartProducts()

        // then
        assert(viewModel.cartProducts.value?.size == 3)
    }

    @Test
    fun `장바구니 상품들을 삭제할 수 있다`() {
        // given
        val id = 0

        // when
        viewModel.deleteCartProduct(id)

        // then
        assert(viewModel.onCartProductDeleted.value == true)
    }
}
