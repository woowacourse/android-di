package woowacourse.shopping.ui.cart

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.ui.cart.ProductFixture.Products

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = mockk()
        cartViewModel = CartViewModel(cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니의 모든 상품을 불러온다`() {
        // given
        val cartProducts = Products().map { it.toEntity().toModel() }

        coEvery {
            cartRepository.getAllCartProducts()
        } answers { cartProducts }

        // when
        cartViewModel.getAllCartProducts()

        // then
        val actual = cartViewModel.cartProducts.value ?: listOf()
        assertEquals(cartProducts, actual)
    }

    @Test
    fun `장바구니에서 상품을 삭제하면 onCartProductDeleted 값이 true가 된다`() {
        // given
        val productId = 0L

        coEvery {
            cartRepository.deleteCartProduct(productId)
        } answers { nothing }

        // when
        cartViewModel.deleteCartProduct(productId)

        // then
        val actual = cartViewModel.onCartProductDeleted.value ?: false
        assertTrue(actual)
    }
}
