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
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AutoDIViewModelFactory
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.fake.PRODUCTS
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val dependencies =
            mapOf(
                CartRepository::class to FakeCartRepository(),
                ProductRepository::class to FakeProductRepository(),
            )

        val factory = AutoDIViewModelFactory(dependencies)
        mainViewModel = factory.create(MainViewModel::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `카트에 품목을 추가하면 onProductAdded 의 값이 true 가 된다`() =
        runTest {
            // given:
            // when:
            mainViewModel.addCartProduct(
                Product(
                    "떡뻥",
                    2000,
                    "",
                ),
            )

            advanceUntilIdle()

            // then:
            val onProductAdded = mainViewModel.onProductAdded.getOrAwaitValue()
            assertThat(onProductAdded).isEqualTo(true)
        }

    @Test
    fun `상품을 모두 조회하면 products 에 업데이트 된다`() {
        // given:

        // when:
        mainViewModel.getAllProducts()

        // then:
        val products = mainViewModel.products.getOrAwaitValue()
        assertSoftly {
            assertThat(products.size).isEqualTo(2)
            assertThat(products).isEqualTo(PRODUCTS)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
