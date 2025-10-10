package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        val cartRepository = FakeCartRepository().apply { addCartProduct(PRODUCT_1) }
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 데이터를 불러올 수 있다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        val actual: List<Product> = viewModel.cartProducts.getOrAwaitValue()
        val expected: List<Product> = listOf(PRODUCT_1)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에서 특정 인덱스에 있는 상품을 제거할 수 있다`() {
        // when
        viewModel.deleteCartProduct(0)
        viewModel.getAllCartProducts()

        // then
        val actual: List<Product> = viewModel.cartProducts.getOrAwaitValue()
        val expected: List<Product> = listOf()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 담긴 상품을 제거하면 상품 제거 이벤트가 발생한다`() {
        // when
        viewModel.deleteCartProduct(0)

        // then
        val actual: Boolean = viewModel.onCartProductDeleted.getOrAwaitValue()
        assertThat(actual).isTrue()
    }
}
