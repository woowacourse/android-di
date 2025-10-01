package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.fixture.FakeCartRepository
import woowacourse.fixture.FakeProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cartRepository = FakeCartRepository()
    private val viewModel = CartViewModel(cartRepository)

    private fun product(name: String) = Product(name, 10_000, "")

    @Test
    fun `장바구니에 저장된 상품을 조회할 수 있다`() {
        cartRepository.apply {
            addCartProduct(product("A"))
            addCartProduct(product("B"))
        }

        viewModel.getAllCartProducts()
        val expected = cartRepository.getAllCartProducts()
        val actual = viewModel.cartProducts.value

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에 저장된 상품을 삭제할 수 있다`() {
        cartRepository.apply {
            addCartProduct(product("A"))
            addCartProduct(product("B"))
            addCartProduct(product("C"))
        }

        viewModel.deleteCartProduct(1) // B 삭제
        viewModel.getAllCartProducts()

        assertThat(viewModel.cartProducts.value!!.map { it.name }).containsExactly("A", "C")
        assertThat(viewModel.onCartProductDeleted.value).isTrue()
    }
}
