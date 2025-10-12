package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.PRODUCTS_FIXTURE
import woowacourse.shopping.fixture.PRODUCT_FIXTURE
import woowacourse.shopping.fixture.repository.FakeCartRepository
import woowacourse.shopping.fixture.repository.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        productRepository = FakeProductRepository(PRODUCTS_FIXTURE)
        cartRepository = FakeCartRepository(mutableListOf())
        viewModel = MainViewModel(productRepository, cartRepository)
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
