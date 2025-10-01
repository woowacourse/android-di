package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.textfixture.FakeCartRepository
import woowacourse.shopping.textfixture.TEST_PRODUCTS
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartProducts: MutableList<Product>

    @Before
    fun setUp() {
        cartProducts = TEST_PRODUCTS.toMutableList()
        cartViewModel = CartViewModel(FakeCartRepository(cartProducts))
    }

    @Test
    fun `모든 장바구니 상품을 조회할 수 있다`() {
        // given

        // when
        cartViewModel.getAllCartProducts()

        // then
        Assert.assertEquals(cartProducts, cartViewModel.cartProducts.getOrAwaitValue())
    }

    @Test
    fun `상품을 제거할 수 있다`() {
        // given

        // when
        cartViewModel.deleteCartProduct(0)

        // then
        Assert.assertTrue(cartViewModel.onCartProductDeleted.getOrAwaitValue())
    }
}
