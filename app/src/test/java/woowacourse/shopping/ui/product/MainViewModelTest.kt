package woowacourse.shopping.ui.product

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.di.DependencyContainer
import com.example.di.ViewModelFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.FakeRepositoryModule
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2
import woowacourse.shopping.fixture.PRODUCT_3
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val application = ApplicationProvider.getApplicationContext<Application>()
        DependencyContainer.initialize(application, FakeRepositoryModule())
        viewModel = MainViewModel()
        viewModel = ViewModelFactory.create(MainViewModel::class.java)
        viewModel.getAllProducts()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품 데이터를 불러올 수 있다`() {
        val actual: List<Product> = viewModel.products.getOrAwaitValue()
        val expected: List<Product> = listOf(PRODUCT_1, PRODUCT_2, PRODUCT_3)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 상품을 추가하면 상품 추가 이벤트가 발생한다`() =
        runTest {
            // when
            viewModel.addCartProduct(PRODUCT_1)
            advanceUntilIdle()

            // then
            val actual: Boolean = viewModel.onProductAdded.getOrAwaitValue()
            assertThat(actual).isTrue()
        }
}
