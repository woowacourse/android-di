package woowacourse.shopping.ui.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.AppContainer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.fixture.ProductsFixture
import woowacourse.shopping.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        AppContainer.provide<CartRepository>(FakeCartRepository())
        AppContainer.provide<ProductRepository>(FakeProductRepository(ProductsFixture))
        viewModel = AppContainer.resolve()
    }

    @Test
    fun `전체 상품 조회`() =
        runTest {
            // when
            viewModel.getAllProducts()

            // then
            val products = viewModel.products.getOrAwaitValue()

            assertSoftly { softly ->
                softly.assertThat(products).hasSize(3)
                softly
                    .assertThat(products)
                    .extracting("name")
                    .containsExactly("우테코 과자", "우테코 쥬스", "우테코 아이스크림")
            }
        }

    @Test
    fun `상품 추가`() =
        runTest {
            // when
            viewModel.addCartProduct(ProductFixture)
            advanceUntilIdle()

            // then
            val onProductAdded = viewModel.onProductAdded.getOrAwaitValue()
            assertThat(onProductAdded).isTrue
        }
}
