package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.PRODUCT_FIXTURE

class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        cartRepository = CartRepositoryImpl()
    }

    @Test
    fun `장바구니 상품 전체 조회 테스트`() {
        // given
        val product = PRODUCT_FIXTURE
        cartRepository.addCartProduct(product)

        // when
        val cartProducts = cartRepository.getAllCartProducts()

        // then
        assertThat(cartProducts).isEqualTo(listOf(product))
    }

    @Test
    fun `장바구니 상품 추가 테스트`() {
        // given
        val product = PRODUCT_FIXTURE

        // when
        cartRepository.addCartProduct(product)

        // then
        val cartProducts = cartRepository.getAllCartProducts()
        assertThat(cartProducts).contains(product)
    }

    @Test
    fun `장바구니 상품 삭제 테스트`() {
        // given
        val product = PRODUCT_FIXTURE
        cartRepository.addCartProduct(product)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        val cartProducts = cartRepository.getAllCartProducts()
        assertThat(cartProducts).doesNotContain(product)
    }
}
