package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.productsFixture
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `전체 상품을 불러올 수 있다`() {
        // When
        viewModel.getAllProducts()

        // Then
        val loadedProducts = viewModel.products.getOrAwaitValue()
        assertThat(loadedProducts).isEqualTo(productsFixture)
    }

    @Test
    fun `장바구니에 상품을 추가하면 onProductAdded 이벤트가 발생한다`() {
        // When
        viewModel.addCartProduct(productsFixture[0])

        // Then
        val isAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(isAdded).isTrue()
    }
}
