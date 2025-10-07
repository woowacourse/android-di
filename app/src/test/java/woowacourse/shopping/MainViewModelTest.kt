package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.fixture.FakeCartRepository
import woowacourse.fixture.FakeProductRepository
import woowacourse.fixture.PRODUCT1
import woowacourse.fixture.getOrAwaitValue
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        productRepository =
            FakeProductRepository(
                fakeAllProducts =
                    listOf(
                        PRODUCT1,
                    ),
            )
        cartRepository =
            FakeCartRepository(
                fakeAllCartProducts =
                    listOf(
                        PRODUCT1,
                    ),
            )
        viewModel =
            MainViewModel(
                productRepository,
                cartRepository,
            )
    }

    @Test
    fun `상품을 추가할 수 있다`() {
        // given
        val product = PRODUCT1

        // when
        viewModel.addCartProduct(product)

        // then
        val productAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(productAdded).isTrue()
    }

    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given
        val expected = productRepository.getAllProducts()

        // when
        viewModel.getAllProducts()

        // then
        val actual = viewModel.products.getOrAwaitValue()
        assertThat(actual).isEqualTo(expected)
    }
}
