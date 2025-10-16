package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.productsFixture
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        val fakeProductRepository = FakeProductRepository()
        val fakeCartRepository = FakeCartRepository()

        viewModel =
            MainViewModel().apply {
                productRepository = fakeProductRepository
                cartRepository = fakeCartRepository
            }
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
    fun `장바구니에 상품을 추가하면 onProductAdded 이벤트가 발생한다`() =
        runTest {
            // When
            viewModel.addCartProduct(productsFixture[0])

            // Then
            val isAdded = viewModel.onProductAdded.getOrAwaitValue()
            assertThat(isAdded).isTrue()
        }
}
