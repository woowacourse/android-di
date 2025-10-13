package woowacourse.shopping

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
import woowacourse.fixture.FakeCartRepository
import woowacourse.fixture.FakeProductRepository
import woowacourse.fixture.PRODUCT1
import woowacourse.fixture.getOrAwaitValue
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        productRepository =
            FakeProductRepository(
                fakeAllProducts =
                    listOf(
                        PRODUCT1,
                    ),
            )
        cartRepository =
            FakeCartRepository(
                fakeAllCartProducts =
                    listOf(
                        PRODUCT1,
                    ),
            )
        viewModel = MainViewModel()
        viewModel.productRepository = productRepository
        viewModel.cartRepository = cartRepository
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 추가할 수 있다`() =
        runTest {
            // given
            val product = PRODUCT1

            // when
            viewModel.addCartProduct(product)
            advanceUntilIdle()

            // then
            val productAdded = viewModel.onProductAdded.getOrAwaitValue()
            assertThat(productAdded).isTrue()
        }

    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given
        val expected = productRepository.getAllProducts()

        // when
        viewModel.getAllProducts()

        // then
        val actual = viewModel.products.getOrAwaitValue()
        assertThat(actual).isEqualTo(expected)
    }
}
