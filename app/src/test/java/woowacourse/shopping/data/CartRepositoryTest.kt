package woowacourse.shopping.data

import junit.framework.TestCase.assertEquals
import org.junit.Test
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.model.Product

class CartRepositoryTest {

    @Test
    fun 장바구니의_모든_상품을_가져온다() {
        // given
        val products = listOf(Product("", 0, ""))
        val cartRepository = FakeCartRepository(products)

        // when
        val actual = cartRepository.getAllCartProducts()

        // then
        assertEquals(products, actual)
    }

    @Test
    fun 장바구니의_0번째_상품을_삭제한다() {
        // given
        val products = listOf(Product("", 0, ""), Product("", 1, ""))
        val cartRepository = FakeCartRepository(products)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        val expect = listOf(Product("", 1, ""))
        val actual = cartRepository.products

        assertEquals(expect, actual)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun 장바구니에_존재하지_않는_상품을_삭제하면_IndexOutOfBoundsException_예외가_발생한다() {
        // given
        val products = emptyList<Product>()
        val cartRepository = FakeCartRepository(products)

        // when
        cartRepository.deleteCartProduct(0)

        // then
    }
}
