package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.fixture.GOBCHANG
import woowacourse.shopping.fixture.MALATANG
import woowacourse.shopping.fixture.TONKATSU
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
        productRepository = FakeProductRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `물건_목록을_불러온다`() {
        // when
        viewModel.getAllProducts()
        val products = viewModel.products.getOrAwaitValue()

        // then
        assertTrue(products.containsAll(listOf(TONKATSU, MALATANG, GOBCHANG)))
    }

    @Test
    fun `카트에_상품을_추가한다`() {
        // when
        viewModel.addCartProduct(TONKATSU)
        val isAdded = viewModel.onProductAdded.getOrAwaitValue()

        // then
        assertTrue(isAdded)
    }
}
