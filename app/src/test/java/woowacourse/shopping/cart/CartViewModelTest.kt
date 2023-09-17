package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.Dummy.cartProduct
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 불러오면 cartProducts에 장바구니 상품의 전체 목록이 저장된다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        assertEquals(listOf(cartProduct), viewModel.cartProducts.getOrAwaitValue())
    }

    @Test
    fun `장바구니에 담김 상품을 삭제하면 onCartProductDeleted의 값이 true가 된다`() {
        // when
        viewModel.deleteCartProduct(0)

        // then
        assertEquals(true, viewModel.onCartProductDeleted.getOrAwaitValue())
    }
}
