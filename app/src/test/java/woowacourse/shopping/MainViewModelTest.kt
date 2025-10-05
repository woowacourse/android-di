package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2
import woowacourse.shopping.fixture.PRODUCT_3
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `상품 데이터를 불러올 수 있다`() {
        // given
        val viewModel = MainViewModel(FakeProductRepository(), FakeCartRepository())

        // when
        viewModel.getAllProducts()

        // then
        val actual: List<Product> = viewModel.products.getOrAwaitValue()
        val expected: List<Product> = listOf(PRODUCT_1, PRODUCT_2, PRODUCT_3)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // given
        val productRepository = FakeProductRepository()
        val cartRepository = FakeCartRepository()
        val viewModel = MainViewModel(productRepository, cartRepository)

        // when
        viewModel.addCartProduct(PRODUCT_1)

        // then
        val actual: List<Product> = cartRepository.getAllCartProducts()
        val expected: List<Product> = listOf(PRODUCT_1)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 상품을 추가하면 상품 추가 이벤트가 발생한다`() {
        // given
        val viewModel = MainViewModel(FakeProductRepository(), FakeCartRepository())

        // when
        viewModel.addCartProduct(PRODUCT_1)

        // then
        val actual: Boolean = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(actual).isTrue()
    }
}
