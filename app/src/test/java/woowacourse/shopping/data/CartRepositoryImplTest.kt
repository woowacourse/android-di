package woowacourse.shopping.data

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.util.Dummy

class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl(
            Dummy.cartProducts.toMutableList(),
        )
    }

    @Test
    fun `모든 카트 상품들을 반환한다`() {
        // when
        val actual = cartRepository.getAllCartProducts()
        // then
        val expected = Dummy.cartProducts
        assertEquals(expected, actual)
    }

    @Test
    fun `카트 상품을 지울 수 있다`() {
        // when
        cartRepository.deleteCartProduct(0)
        // then
        val actual = cartRepository.getAllCartProducts()
        val expected = listOf(Dummy.cartProducts[1])
        assertEquals(expected, actual)
    }

    @Test
    fun `카트 상품을 추가할 수 있다`() {
        // when
        cartRepository.addCartProduct(product = Dummy.product)
        // then
        val actual = cartRepository.getAllCartProducts()
        val expected = Dummy.cartProducts + Dummy.product
        assertEquals(expected, actual)
    }
}
