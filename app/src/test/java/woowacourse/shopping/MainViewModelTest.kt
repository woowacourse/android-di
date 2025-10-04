package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.containerProvider
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val application =
        RuntimeEnvironment.getApplication()

    @Test
    fun `상품을 추가할 수 있다`() {
        // given
        val viewModel by application.containerProvider<MainViewModel>()
        val product = Product("상품명", 1000, "이미지URL")

        // when
        viewModel.addCartProduct(product)

        // then
        val productAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(productAdded).isTrue()
    }

    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given
        val viewModel by application.containerProvider<MainViewModel>()
        val productRepository by application.containerProvider<DefaultProductRepository>()
        val expected = productRepository.getAllProducts()

        // when
        viewModel.getAllProducts()

        // then
        val actual = viewModel.products.getOrAwaitValue()
        assertThat(actual).isEqualTo(expected)
    }
}
