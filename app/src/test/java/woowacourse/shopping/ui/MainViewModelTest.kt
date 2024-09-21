package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.DIContainer
import kotlinx.coroutines.Dispatchers
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
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.FakeProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        DIContainer.setInstance(ProductRepository::class, productRepository, "room")
        DIContainer.setInstance(CartRepository::class, cartRepository, "room")
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기에 상품을 모두 불러온다`() {
        println("viewModel: $viewModel")
        println("productRepository: $productRepository")

        // when
        viewModel.getAllProducts()

        // then
        val products = productRepository.getAllProducts()
        assert(viewModel.products.value == products)
    }

    @Test
    fun `카트에 상품 추가`() {
        runTest {
            // given
            val product = productRepository.getAllProducts().first()

            // when
            viewModel.addCartProduct(product)
            advanceUntilIdle()

            // then
            val cartProducts = cartRepository.getAllCartProducts()
            assert(viewModel.onProductAdded.value == true)
            assert(cartProducts.size == 1)
        }
    }
}
