package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
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
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.ui.cart.CartViewModel

@ExperimentalCoroutinesApi
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: DefaultCartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        viewModel = CartViewModel(cartRepository)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 담겨있는 모든 상품 목록을 불러온다`() {
        // given
        coEvery {
            cartRepository.getAllCartProducts()
        } returns CartProductFixture.products

        // when
        viewModel.getAllCartProducts()

        val actual = viewModel.uiState.value?.cartProducts

        // then
        val expected = CartProductFixture.products

        assertEquals(expected, actual)
    }

    @Test
    fun `장바구니에 담겨있는 상품 하나를 삭제하면 장바구니 상품이 삭제된 상태가 된다`() {
        // given
        val id: Long = 0

        coEvery {
            cartRepository.getAllCartProducts()
        } returns CartProductFixture.products

        viewModel.getAllCartProducts()

        // when
        viewModel.deleteCartProduct(id)

        val actual = viewModel.onCartProductDeleted.value

        // then
        val expected = true

        assertEquals(expected, actual)
    }
}
