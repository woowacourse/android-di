package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.productsFixture
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
        assertThat(viewModel.products.value).isEqualTo(productsFixture)
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // Given

        // When
        viewModel.addCartProduct(productsFixture[0])

        // Then
        val cartProducts = cartRepository.getAllCartProducts()
        assertThat(cartProducts).isEqualTo(listOf(productsFixture[0]))
    }
}
