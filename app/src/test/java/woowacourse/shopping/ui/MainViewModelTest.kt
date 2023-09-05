package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.createProduct
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.getProducts
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModelTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `상품을 추가하면 카트에 추가한 상품이 담긴다`() {
        // given

        // when
        val product = createProduct("글로", 1_000_000_000, "")
        viewModel.addCartProduct(product)

        // then
        assertThat(cartRepository.getAllCartProducts()).contains(product)
    }

    @Test
    fun `카트 상품을 추가하면 카트에 상품을 담았는지 여부가 참이 된다`() {
        // given
        assertThat(viewModel.onProductAdded.value).isFalse

        // when
        val product = createProduct("글로", 1_000_000_000, "")
        viewModel.addCartProduct(product)

        // then
        assertThat(viewModel.onProductAdded.value).isTrue
    }

    @Test
    fun `모든 상품을 조회하면 모든 상품을 가져올 수 있다`() {
        // given

        // when
        viewModel.getAllProducts()

        // then
        val expected = getProducts()
        assertThat(viewModel.products.value).isEqualTo(expected)
    }
}
