package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.FakeCartRepository

class CartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `뷰모델을 생성한 뒤 장바구니에 있는 모든 상품을 받아온 경우 개수는 0개다`() {
        // given
        val viewModel = CartViewModel(FakeCartRepository())

        // when
        viewModel.getAllCartProducts()
        val cartProducts = viewModel.cartProducts
        val expected = cartProducts.value?.size

        // then
        assertEquals(expected, 0)
    }

    @Test
    fun `장바구니에서 아이템을 삭제하지 않은 경우 OnCartProductDeleted는 false다`() {
        // given
        val fakeCartRepository = FakeCartRepository()
        val product = Product("과자", 100, "")
        fakeCartRepository.addCartProduct(product)

        val viewModel = CartViewModel(fakeCartRepository)

        // when
        val expected = viewModel.onCartProductDeleted.value!!

        // then
        assertFalse(expected)
    }

    @Test
    fun `장바구니에서 아이템을 삭제하면 OnCartProductDeleted가 true다`() {
        // given
        val fakeCartRepository = FakeCartRepository()
        val product = Product("과자", 100, "")
        fakeCartRepository.addCartProduct(product)

        val viewModel = CartViewModel(fakeCartRepository)

        // when
        viewModel.deleteCartProduct(0)
        val expected = viewModel.onCartProductDeleted.value!!

        // then
        assertTrue(expected)
    }
}
