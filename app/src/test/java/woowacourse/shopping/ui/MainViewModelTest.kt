package woowacourse.shopping.ui

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.Container
import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.di.annotation.RoomDB
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class MainViewModelTest {
    private lateinit var container: Container
    private lateinit var viewModel: MainViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        container =
            Container().apply {
                bind(ProductRepository::class) { FakeProductRepository() }
                bind(
                    type = CartRepository::class,
                    provider = { FakeCartRepository() },
                    qualifier = RoomDB::class,
                )
            }
        viewModel = container.get()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @DisplayName("상품을 추가하면 상태가 변경된다")
    @Test
    fun addCartProductTest() =
        runTest(testDispatcher) {
            // when
            viewModel.addCartProduct(DEFAULT_PRODUCT)
            advanceUntilIdle()

            // then
            viewModel.onProductAdded.getOrAwaitValue().shouldBeTrue()
        }

    @DisplayName("상품 목록을 가져온다")
    @Test
    fun getAllProductsTest() =
        runTest(testDispatcher) {
            // when
            viewModel.getAllProducts()
            val products = viewModel.products.getOrAwaitValue()

            // then
            products.shouldContain(DEFAULT_PRODUCT)
        }
}
