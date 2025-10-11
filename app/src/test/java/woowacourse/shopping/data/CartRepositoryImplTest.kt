package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.fixture.ProductsFixture

class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl()
    }

    @Test
    fun `장바구니에 단일 상품 추가`() {
        cartRepository.addCartProduct(ProductFixture)

        // then
        assertThat(cartRepository.getAllCartProducts()).containsExactly(ProductFixture)
    }

    @Test
    fun `상품 삭제`() {
        // given
        cartRepository.addCartProduct(ProductsFixture[0])
        cartRepository.addCartProduct(ProductsFixture[1])

        // when
        cartRepository.deleteCartProduct(0)

        // then
        assertThat(cartRepository.getAllCartProducts()).doesNotContain(ProductsFixture[0])
    }

    @Test
    fun `존재하지 않는 인덱스 삭제 시 예외 발생`() {
        // given
        cartRepository.addCartProduct(ProductFixture)

        // when & then
        assertThatThrownBy { cartRepository.deleteCartProduct(5) }
            .isInstanceOf(IndexOutOfBoundsException::class.java)
    }

    @Test
    fun `추가와 삭제를 반복하여 일관성 유지`() {
        // given
        // when
        cartRepository.addCartProduct(ProductsFixture[0])
        cartRepository.addCartProduct(ProductsFixture[1])
        cartRepository.deleteCartProduct(0)
        cartRepository.addCartProduct(ProductsFixture[2])

        // then
        assertThat(cartRepository.getAllCartProducts())
            .containsExactly(
                ProductsFixture[1],
                ProductsFixture[2],
            )
    }
}
