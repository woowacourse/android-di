package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.FakeProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        productRepository = FakeProductRepository()
        cartRepository = InMemoryCartRepository()
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `카트에 상품을 추가할 수 있다`() {
        // given
        val product = Product(1L, "Product1", 1000, "image1")

        // when
        viewModel.addCartProduct(product)

        // then
        val productAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(productAdded).isTrue()
    }

    @Test
    fun `모든 상품을 가져올 수 있다`() {
        // when
        viewModel.getAllProducts()

        // then
        val products = viewModel.products.getOrAwaitValue()
        assertThat(products).isEqualTo(
            listOf(
                Product(1, "Product1", 1000, "image1"),
                Product(2, "Product2", 2000, "image2"),
                Product(3, "Product3", 3000, "image3"),
            ),
        )
    }
}
