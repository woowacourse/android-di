package woowacourse.shopping.cart

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.textfixture.TEST_PRODUCT

class CartRepositoryTest {
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl()
    }

    @Test
    fun `상품을 추가하면 장바구니에 해당 상품이 존재한다`() {
        // given
        cartRepository.addCartProduct(TEST_PRODUCT)
        // when
        val actual = cartRepository.getAllCartProducts()

        // then
        Assert.assertEquals(mutableListOf(TEST_PRODUCT), actual)
    }

    @Test
    fun `상품을 제거하면 장바구니에 해당 상품이 존재하지 않는다`() {
        // given
        cartRepository.addCartProduct(TEST_PRODUCT)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        val actual = cartRepository.getAllCartProducts()
        Assert.assertEquals(emptyList<Product>(), actual)
    }
}
