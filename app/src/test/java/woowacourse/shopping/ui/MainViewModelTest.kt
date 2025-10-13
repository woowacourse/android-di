package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.m6z1.moongdi.AutoDIViewModelFactory
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
import woowacourse.shopping.fake.FakeApplication
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
        val factory = AutoDIViewModelFactory<FakeApplication>()
        val extras =
            MutableCreationExtras().apply {
                this[APPLICATION_KEY] = FakeApplication()
            }
        mainViewModel = factory.create(MainViewModel::class.java, extras)
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
