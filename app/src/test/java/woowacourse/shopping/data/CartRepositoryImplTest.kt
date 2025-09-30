package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.model.Product

class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl()
    }

    @Test
    fun `장바구니에 단일 상품 추가`() {
        // given
        val apple = Product("사과", 1000, "url://apple")

        // when
        cartRepository.addCartProduct(apple)

        // then
        assertThat(cartRepository.getAllCartProducts()).containsExactly(apple)
    }

    @Test
    fun `상품 삭제`() {
        // given
        val apple = Product("사과", 1000, "url://apple")
        val banana = Product("바나나", 2000, "url://banana")
        cartRepository.addCartProduct(apple)
        cartRepository.addCartProduct(banana)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        assertThat(cartRepository.getAllCartProducts()).doesNotContain(apple)
    }

    @Test
    fun `존재하지 않는 인덱스 삭제 시 예외 발생`() {
        // given
        val apple = Product("사과", 1000, "url://apple")
        cartRepository.addCartProduct(apple)

        // when & then
        assertThatThrownBy { cartRepository.deleteCartProduct(5) }
            .isInstanceOf(IndexOutOfBoundsException::class.java)
    }

    @Test
    fun `추가와 삭제를 반복하여 일관성 유지`() {
        // given
        val apple = Product("사과", 1000, "url://apple")
        val banana = Product("바나나", 2000, "url://banana")
        val orange = Product("오렌지", 1500, "url://orange")

        // when
        cartRepository.addCartProduct(apple)
        cartRepository.addCartProduct(banana)
        cartRepository.deleteCartProduct(0) // 사과 삭제
        cartRepository.addCartProduct(orange)

        // then
        assertThat(cartRepository.getAllCartProducts()).containsExactly(banana, orange)
    }
}
