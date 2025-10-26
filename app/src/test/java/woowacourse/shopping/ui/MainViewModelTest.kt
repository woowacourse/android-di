package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.di.di.InjectorViewModelFactory
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
@Config(application = ShoppingApplication::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        mainViewModel =
            InjectorViewModelFactory(
                dependencyInjector = (application as ShoppingApplication).dependencyInjector,
                scopeHolder = this,
            ).create(MainViewModel::class.java)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 추가하면 상품 추가 여부가 반영된다`() =
        runTest {
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
    fun `상품을 가져오면 상품들이 반영된다`() =
        runTest {
            // given & when
            mainViewModel.getAllProducts()
            advanceUntilIdle()

            // then
            val products = mainViewModel.products.getOrAwaitValue()

            assertThat(products.isNotEmpty()).isEqualTo(true)
        }
}
