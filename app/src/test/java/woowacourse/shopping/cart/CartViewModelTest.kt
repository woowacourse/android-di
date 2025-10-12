package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.TEST_PRODUCTS
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.ui.cart.CartViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        cartRepository = mockk<CartRepository>()
        cartViewModel = CartViewModel()
        val field = CartViewModel::class.java.getDeclaredField("cartRepository")
        field.isAccessible = true
        field.set(cartViewModel, cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `모든 장바구니 상품을 조회할 수 있다`() =
        runTest {
            // given
            coEvery { cartRepository.getAllCartProducts() } returns Result.success(TEST_PRODUCTS)

            // when
            cartViewModel.getAllCartProducts()
            advanceUntilIdle()
            // then
            coVerify { cartRepository.getAllCartProducts() }
            Assert.assertEquals(
                TEST_PRODUCTS,
                cartViewModel.cartProducts.getOrAwaitValue(),
            )
        }

    @Test
    fun `상품을 제거할 수 있다`() =
        runTest {
            // given
            coEvery { cartRepository.getAllCartProducts() } returns Result.success(TEST_PRODUCTS)
            coEvery { cartRepository.deleteCartProduct(1L) } returns Result.success(Unit)

            // when
            cartViewModel.getAllCartProducts()
            cartViewModel.deleteCartProduct(1L)
            advanceUntilIdle()

            // then
            coVerify { cartRepository.deleteCartProduct(1L) }
            coVerify { cartRepository.getAllCartProducts() }
            val result = cartViewModel.onCartProductDeleted.getOrAwaitValue()
            Assert.assertTrue(result)
        }
}
