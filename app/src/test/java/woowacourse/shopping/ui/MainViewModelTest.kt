package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private val mainViewModel = MainViewModel().apply {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
    }

    @Test
    fun `상품을 추가하면 상품 추가 여부가 반영된다`() = runTest {
        // given
        val product = Product(0, "과자", 1000, "", createdAt = 0L)

        // when
        mainViewModel.addCartProduct(product)
        advanceUntilIdle()

        // then
        val isProductAdded = mainViewModel.onProductAdded.getOrAwaitValue()
        assertThat(isProductAdded).isEqualTo(true)
    }

    @Test
    fun `상품을 가져오면 상품들이 반영된다`() = runTest {
        // given & when
        mainViewModel.getAllProducts()
        advanceUntilIdle()

        // then
        val products = mainViewModel.products.getOrAwaitValue()

        assertThat(products.isNotEmpty()).isEqualTo(true)
    }
}
