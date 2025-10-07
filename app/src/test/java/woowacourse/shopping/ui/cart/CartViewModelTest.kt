package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `장바구니 상품을 조회하면 목록에 반영된다`() {
        // given
        val cartViewModel =
            CartViewModel(
                FakeCartRepository().apply {
                    addCartProduct(Product("과자", 1000, ""))
                    addCartProduct(Product("음료수", 1000, ""))
                },
            )

        // when
        cartViewModel.getAllCartProducts()

        // then
        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts.isNotEmpty()).isEqualTo(true)
    }

    @Test
    fun `장바구니 상품을 삭제하면 목록에 반영된다`() {
        // given
        val cartViewModel =
            CartViewModel(
                FakeCartRepository().apply {
                    addCartProduct(Product("과자", 1000, ""))
                    addCartProduct(Product("음료수", 1000, ""))
                },
            )

        // when
        cartViewModel.deleteCartProduct(0)
        cartViewModel.getAllCartProducts()

        // then
        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts.size).isEqualTo(1)
    }
}
