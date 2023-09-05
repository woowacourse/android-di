package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var fakeCartRepository: FakeCartRepository
    private lateinit var products: List<Product>

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        products = listOf(
            Product("픽셀 폴드", 1500000, ""),
            Product("갤럭시 z폴드 5", 1800000, ""),
            Product("갤럭시 z플립 5", 1200000, ""),
        )
        fakeCartRepository = FakeCartRepository(products)
        cartViewModel = CartViewModel(fakeCartRepository)
    }

    @Test
    fun `장바구니의 모든 상품을 불러오면 cartProducts에 저장된다`() {
        // given
        val expected = products.size

        // when
        cartViewModel.getAllCartProducts()
        val actual = cartViewModel.cartProducts.value?.size

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `장바구니에서 상품을 지우면 onCartProductDeleted가 false에서 true가 된다`() {
        // given
        val initialOnCartProductDeleted = cartViewModel.onCartProductDeleted.value

        // when
        cartViewModel.deleteCartProduct(0)
        val actual = cartViewModel.onCartProductDeleted.value

        // then
        assertEquals(initialOnCartProductDeleted, false)
        assertEquals(actual, true)
    }
}
