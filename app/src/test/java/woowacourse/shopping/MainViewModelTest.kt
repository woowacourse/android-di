package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
import woowacourse.shopping.app.ui.MainViewModel
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var productRepository: FakeProductRepository
    private lateinit var cartRepository: FakeCartRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel()

        viewModel.productRepository = productRepository
        viewModel.cartRepository = cartRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `전체 상품을 조회할 수 있다`() =
        runTest {
            viewModel.getAllProducts()

            val expected = productRepository.getAllProducts()
            val actual = viewModel.products.getOrAwaitValue()

            assertThat(actual).isEqualTo(expected)
            assertThat(actual).isNotEmpty()
        }

    @Test
    fun `상품을 저장하여 장바구니에 추가할 수 있다`() =
        runTest {
            val selectedProduct: Product = productRepository.getAllProducts().first()

            viewModel.addCartProduct(selectedProduct)

            val cartProducts = cartRepository.getAllCartProducts()
            assertThat(cartProducts).contains(selectedProduct)

            assertThat(viewModel.onProductAdded.getOrAwaitValue()).isTrue()
        }
}
