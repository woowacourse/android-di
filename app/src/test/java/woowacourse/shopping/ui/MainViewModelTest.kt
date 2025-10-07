package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mainViewModel = MainViewModel(FakeProductRepository(), FakeCartRepository())

    @Test
    fun `상품을 추가하면 상품 추가 여부가 반영된다`() {
        // given
        val product = Product("과자", 1000, "")

        // when
        mainViewModel.addCartProduct(product)

        // then
        val isProductAdded = mainViewModel.onProductAdded.getOrAwaitValue()
        assertThat(isProductAdded).isEqualTo(true)
    }

    @Test
    fun `상품을 가져오면 상품들이 반영된다`() {
        // given & when
        mainViewModel.getAllProducts()

        // then
        val products = mainViewModel.products.getOrAwaitValue()

        assertThat(products.isNotEmpty()).isEqualTo(true)
    }
}
