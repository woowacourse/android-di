package woowacourse.shopping.ui.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.fixture.ProductsFixture
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        AppContainer.provide(CartRepository::class, FakeCartRepository())
        AppContainer.provide(ProductRepository::class, FakeProductRepository(ProductsFixture))
        viewModel = AppContainer.resolve(MainViewModel::class)
    }

    @Test
    fun `전체 상품 조회`() {
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
    fun `상품 추가`() {
        // when
        viewModel.addCartProduct(ProductFixture)

        // then
        val onProductAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(onProductAdded).isTrue
    }
}
