package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instant = InstantTaskExecutorRule()

    @Test
    fun `장바구니_상품을_조회하면_순서대로_조회된다`() {
        // given
        val repository: CartRepository = CartRepositoryImpl()
        val a = Product("A", 10, "a")
        val b = Product("B", 20, "b")
        repository.addCartProduct(a)
        repository.addCartProduct(b)
        val viewModel = CartViewModel(repository)

        // when
        viewModel.getAllCartProducts()

        // then
        assertThat(viewModel.cartProducts.getOrAwaitValue()).containsExactly(a, b).inOrder()
    }

    @Test
    fun `장바구니_상품을_삭제하면_삭제_이벤트가_발생하고_해당_상품이_제거된다`() {
        // given
        val repository: CartRepository = CartRepositoryImpl()
        val a = Product("A", 10, "a")
        val b = Product("B", 20, "b")
        repository.addCartProduct(a)
        repository.addCartProduct(b)
        val viewModel = CartViewModel(repository)

        // when
        viewModel.deleteCartProduct(1)

        // then
        assertThat(viewModel.onCartProductDeleted.getOrAwaitValue()).isTrue()
        viewModel.getAllCartProducts()
        assertThat(viewModel.cartProducts.getOrAwaitValue()).containsExactly(a)
    }
}
