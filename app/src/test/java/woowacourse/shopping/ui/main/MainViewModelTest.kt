package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultFakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @ExperimentalCoroutinesApi
    fun setupCoroutine() {
        // Dispatcher 상태를 Unconfined 로 변경
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Before
    fun setupMainViewModel() {
        val cartRepository: CartRepository = DefaultFakeCartRepository()

        viewModel = MainViewModel(cartRepository)
    }

    @Test
    fun `상품을 장바구니에 추가할 수 있다`() {
        // given
        val product: Product = ProductFixture.product

        // when
        viewModel.addCartProduct(product)

        // then
        viewModel.onProductAdded.getOrAwaitValue()

        val expectedIsAdded = true
        assertEquals(expectedIsAdded, viewModel.onProductAdded.value)
    }

    @Test
    fun `상품 정보를 불러온다`() {
        // given

        // when
        viewModel.getAllProducts()

        // then
        viewModel.products.getOrAwaitValue()

        val expectedProducts = ProductFixture.products
        assertEquals(expectedProducts, viewModel.products.value)
    }
}
