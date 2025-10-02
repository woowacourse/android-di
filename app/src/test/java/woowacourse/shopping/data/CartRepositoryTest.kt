package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.model.Product

class CartRepositoryTest {
    @Test
    fun `장바구니에_상품을_추가하면_순서대로_조회된다`() {
        // given
        val repository: CartRepository = CartRepositoryImpl()
        val a = Product("A", 1000, "a")
        val b = Product("B", 2000, "b")

        // when
        repository.addCartProduct(a)
        repository.addCartProduct(b)
        val result = repository.getAllCartProducts()

        // then
        assertThat(result).containsExactly(a, b).inOrder()
    }

    @Test
    fun `장바구니에서_위치에_따라_상품을_삭제하면_해당_상품이_제거된다`() {
        // given
        val repository: CartRepository = CartRepositoryImpl()
        val a = Product("A", 1000, "a")
        val b = Product("B", 2000, "b")
        val c = Product("C", 3000, "c")
        repository.addCartProduct(a)
        repository.addCartProduct(b)
        repository.addCartProduct(c)

        // when
        repository.deleteCartProduct(1)
        val result = repository.getAllCartProducts()

        // then
        assertThat(result).containsExactly(a, c).inOrder()
    }
}
