package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.fixture.FakeAppContainer
import woowacourse.shopping.fixture.PRODUCTS_FIXTURE
import woowacourse.shopping.fixture.PRODUCT_FIXTURE
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.fixture.repository.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        val productRepository = FakeProductRepository(PRODUCTS_FIXTURE)
        val cartRepository = FakeCartRepository(mutableListOf())

        val appContainer = FakeAppContainer(productRepository, cartRepository)
        val viewModelFactory = ViewModelFactory(appContainer)
        viewModel = viewModelFactory.create(MainViewModel::class.java)
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
