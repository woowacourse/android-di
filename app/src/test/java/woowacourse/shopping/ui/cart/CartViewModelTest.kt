package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.ProductsFixture
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        AppContainer.provide(CartRepository::class, FakeCartRepository(ProductsFixture))
        viewModel = AppContainer.resolve(CartViewModel::class)
    }

    @Test
    fun `전체 카트 상품 조회`() {
        // when
        viewModel.getAllCartProducts()

        // then
        val products = viewModel.cartProducts.getOrAwaitValue()

        assertSoftly { softly ->
            softly.assertThat(products).hasSize(3)
            softly
                .assertThat(products)
                .extracting("name")
                .containsExactly("우테코 과자", "우테코 쥬스", "우테코 아이스크림")
        }
    }

    @Test
    fun `카트 상품 삭제`() {
        // when
        viewModel.deleteCartProduct(0)

        // then
        val onCartProductDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()

        assertSoftly { softly ->
            softly.assertThat(onCartProductDeleted).isTrue
            softly.assertThat(cartProducts).doesNotContain(ProductsFixture[0])
        }
    }
}
