package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.fixture.FakeCartRepository
import woowacourse.fixture.FakeProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productRepository = FakeProductRepository()
    private val cartRepository = FakeCartRepository()
    private val viewModel = MainViewModel(productRepository, cartRepository)

    @Test
    fun `전체 상품을 조회할 수 있다`() {
        viewModel.getAllProducts()

        val expected = productRepository.getAllProducts()
        val actual = viewModel.products.value
        assertThat(actual).isEqualTo(expected)
        assertThat(actual).isNotEmpty()
    }

    @Test
    fun `상품을 저장하여 장바구니에 추가할 수 있다`() {
        val selectedProduct: Product = productRepository.getAllProducts().first()

        viewModel.addCartProduct(selectedProduct)

        val cartProducts = cartRepository.getAllCartProducts()
        assertThat(cartProducts).contains(selectedProduct)
        assertThat(viewModel.onProductAdded.value).isTrue()
    }
}
