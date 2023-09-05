package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.ui.cart.ProductFixture.Products

class CartViewModelTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
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
        val products = Products()

        every {
            cartRepository.getAllCartProducts()
        } answers { products }

        // when
        cartViewModel.getAllCartProducts()

        // then
        val actual = cartViewModel.cartProducts.value ?: listOf()
        assertEquals(products, actual)
    }

    @Test
    fun `장바구니에서 상품을 삭제하면 onCartProductDeleted 값이 true가 된다`() {
        // given
        val productId = 0

        every {
            cartRepository.deleteCartProduct(productId)
        } answers { nothing }

        // when
        cartViewModel.deleteCartProduct(productId)

        //then
        val actual = cartViewModel.onCartProductDeleted.value ?: false
        assertTrue(actual)
    }
}