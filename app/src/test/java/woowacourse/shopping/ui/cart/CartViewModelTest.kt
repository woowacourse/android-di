package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartProduct
import woowacourse.util.getCartProduct
import woowacourse.util.getCartProducts

internal class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        cartRepository = mockk()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니에 담긴 목록을 조회할 수 있다`() {
        // given
        coEvery { cartRepository.getAllCartProducts() } returns
            getCartProducts(listOf("사과", "포도", "수박"))

        // when
        viewModel.getAllCartProducts()

        // then
        val actual = viewModel.cartProducts.getOrAwaitValue()
        val expected = getCartProducts(listOf("사과", "포도", "수박"))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 담긴 상품을 삭제할 수 있다`() {
        // given
        val carts: MutableList<CartProduct> =
            getCartProducts(listOf("사과", "포도", "수박")).toMutableList()
        coEvery { cartRepository.getAllCartProducts() } answers { carts.toList() }

        val idSlot = slot<Long>()
        coEvery { cartRepository.deleteCartProduct(capture(idSlot)) } answers {
            carts.removeAt(idSlot.captured.toInt())
        }

        // when
        viewModel.deleteCartProduct(1)
        viewModel.getAllCartProducts()

        // then
        val actual = viewModel.cartProducts.getOrAwaitValue()
        val expected = listOf(getCartProduct(0L, "사과"), getCartProduct(2L, "수박"))
        assertThat(actual).isEqualTo(expected)
    }
}
