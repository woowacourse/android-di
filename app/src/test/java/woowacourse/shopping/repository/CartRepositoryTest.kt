package woowacourse.shopping.repository

import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.provider.Dummy
import woowacourse.shopping.provider.Fake

class CartRepositoryTest {

    private lateinit var cartRepository: CartRepository

    @Before
    internal fun setUp() {
        cartRepository = Fake.CartRepository()
    }

    @Test
    fun 장바구니에_상품을_추가한다() = runTest {
        // given
        val addProduct = Dummy.Product()

        // when
        cartRepository.addCartProduct(addProduct)

        // then
        assertTrue(cartRepository.getAllCartProducts()[0].product == addProduct)
    }

    @Test
    fun 장바구니에_담긴_상품을_제거한다() = runTest {
        // given
        val addProduct = Dummy.Product()
        cartRepository.addCartProduct(addProduct)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        assertTrue(cartRepository.getAllCartProducts().size == 0)
    }
}
