package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
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
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.CartProduct

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository
    private val fakeCartProduct = CartProduct("name", 1000, "imageUrl", 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = mockk()
        vm = CartViewModel(cartRepository = cartRepository)
    }

    @After
    @OptIn(ExperimentalCoroutinesApi::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 담긴 모든 상품을 가져온다`() {
        // given
        val products = listOf(fakeCartProduct)
        coEvery { cartRepository.getAllCartProducts() } returns products

        // when
        vm.getAllCartProducts()

        // then
        assertEquals(products, vm.cartProducts.value)
    }

    @Test
    fun `장바구니에 담긴 상품을 삭제한다`() {
        // given
        coEvery { cartRepository.deleteCartProduct(any()) } just Runs

        // when
        vm.deleteCartProduct(0)

        // then
        assertEquals(true, vm.onCartProductDeleted.value)
    }
}
