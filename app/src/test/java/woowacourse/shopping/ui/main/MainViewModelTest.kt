package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.FakeAppContainer
import woowacourse.shopping.fixture.model.PRODUCTS_FIXTURE
import woowacourse.shopping.fixture.model.PRODUCT_FIXTURE
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.fixture.repository.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val productRepository = FakeProductRepository(PRODUCTS_FIXTURE)
        val cartRepository = FakeCartRepository(mutableListOf())

        val appContainer =
            FakeAppContainer().apply {
                register(ProductRepository::class, productRepository)
                register(CartRepository::class, cartRepository)
            }
        val dependencyInjector = DependencyInjector(appContainer)
        val viewModelFactory = ViewModelFactory(dependencyInjector)
        viewModel = viewModelFactory.create(MainViewModel::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `전체 상품 조회 테스트`() {
        // when
        viewModel.getAllProducts()

        // then
        val products: List<Product> = viewModel.products.getOrAwaitValue()
        assertThat(products).hasSize(3)
        assertThat(products).isEqualTo(PRODUCTS_FIXTURE)
    }

    @Test
    fun `장바구니 상품 추가 테스트`() {
        // given
        val product = PRODUCT_FIXTURE

        // when
        viewModel.addCartProduct(product)

        // then
        val onProductAdded: Boolean = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(onProductAdded).isTrue
    }
}
