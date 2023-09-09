package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {
    private lateinit var mockkCartRepository: CartRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkCartRepository = mockk()
    }

    @Test
    fun `카트에 아이템이 있으면 아이템 리스트를 반환한다`() = runTest {
        // given
        val viewModel = CartViewModel(mockkCartRepository)

        coEvery {
            mockkCartRepository.getAllCartProducts()
        } returns listOf(
            Product("item", 1000, "image")
        )

        // when
        viewModel.getAllCartProducts()

        // then
        assertEquals(viewModel.cartProducts.value?.size, 1)
    }

    @Test
    fun `카트에 아이템이 없으면 빈 리스트를 반환한다`() = runTest {
        // given
        val viewModel = CartViewModel(mockkCartRepository)

        coEvery { mockkCartRepository.getAllCartProducts() } returns emptyList()

        // when
        viewModel.getAllCartProducts()

        // then
        assertEquals(viewModel.cartProducts.value, emptyList<Product>())
    }

    @Test
    fun `아이템 지우기 전에는 지우기 이벤트는 false다`() = runTest {
        // given & when
        val viewModel = CartViewModel(mockkCartRepository)

        coJustRun { mockkCartRepository.deleteCartProduct(1) }

        // then
        assertEquals(viewModel.onCartProductDeleted.value, false)
    }

    @Test
    fun `아이템을 지우면 지우기 이벤트가 true다`() {
        // given
        val viewModel = CartViewModel(mockkCartRepository)
        coJustRun { mockkCartRepository.deleteCartProduct(1) }

        // when
        viewModel.deleteCartProduct(1)

        // then
        assertEquals(viewModel.onCartProductDeleted.value, true)
    }
}
