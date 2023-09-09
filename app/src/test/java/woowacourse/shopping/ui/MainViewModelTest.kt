package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.model.Product

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `상품 저장소에서 모든 상품을 가져온 경우 개수는 2개다`() {
        // given
        val viewModel = MainViewModel(FakeCartRepository())
        viewModel.productRepository = FakeProductRepository()

        // when
        viewModel.getAllProducts()

        // then
        assertEquals(viewModel.products.value?.size, 2)
    }

    @Test
    fun `뷰모델에 상품을 추가한 경우 onProductAdded는 true가 된다`() {
        // given
        val viewModel = MainViewModel(FakeCartRepository())
        viewModel.productRepository = FakeProductRepository()

        val product = Product("우테코 과자", 10_000, "")

        // when
        runBlocking {
            viewModel.addCartProduct(product)
        }

        // then
        assertEquals(viewModel.onProductAdded.value, true)
    }
}
