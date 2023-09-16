package woowacourse.shopping.ui.cart

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.provider.DefaultViewModelTest
import woowacourse.shopping.provider.Dummy
import woowacourse.shopping.provider.Fake
import woowacourse.shopping.repository.CartRepository

internal class CartViewModelTest : DefaultViewModelTest() {

    private lateinit var sut: CartViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = Fake.CartRepository()
        sut = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 제품 전체를 조회한다`() = runTest {
        // given
        val products = cartRepository.getAllCartProducts()

        // when
        sut.fetchAllCartProducts()

        // then
        assertEquals(products, sut.cartProducts.getOrAwaitValue())
    }

    @Test
    fun `장바구니 제품을 삭제하면 장바구니 제품 삭제 상태로 변경한다`() = runTest {
        // given
        val product = Dummy.Product()
        cartRepository.addCartProduct(product)

        val deleteCartProduct = cartRepository.getAllCartProducts().find { it.product == product }!!

        // when
        sut.deleteCartProduct(deleteCartProduct)

        // then
        assertTrue(sut.onCartProductDeleted.getOrAwaitValue())
    }
}
