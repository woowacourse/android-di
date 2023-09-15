package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.dummy.createFakeCartProduct
import woowacourse.shopping.dummy.createFakeCartProducts
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 상품들 가져온다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        assert(viewModel.cartProducts.getOrAwaitValue().size == 10)
        assert(viewModel.cartProducts.getOrAwaitValue() == createFakeCartProducts())
    }

    @Test
    fun `장바구니 상품을 삭제한다`() {
        // given
        viewModel.getAllCartProducts()

        // when
        viewModel.deleteCartProduct(3L)
        val expected = createFakeCartProduct(3L)

        // then
        assert(viewModel.onCartProductDeleted.getOrAwaitValue() == true)
    }
}
