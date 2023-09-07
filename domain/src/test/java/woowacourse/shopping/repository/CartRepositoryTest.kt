package woowacourse.shopping.repository

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import woowacourse.shopping.provider.Dummy
import woowacourse.shopping.provider.Fake

class CartRepositoryTest {

    private lateinit var cartRepository: CartRepository

    @BeforeEach
    internal fun setUp() {
        cartRepository = Fake.CartRepository()
    }

    @Test
    fun 장바구니에_상품을_추가한다() {
        // given
        val addProduct = Dummy.Product()

        // when
        cartRepository.addCartProduct(addProduct)

        // then
        assertTrue(cartRepository.getAllCartProducts().contains(addProduct))
    }

    @Test
    fun 장바구니에_담긴_상품을_제거한다() {
        // given
        val addProduct = Dummy.Product()
        cartRepository.addCartProduct(addProduct)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        assertFalse(cartRepository.getAllCartProducts().contains(addProduct))
    }
}
