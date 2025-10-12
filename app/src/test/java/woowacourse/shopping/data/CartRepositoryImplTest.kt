package woowacourse.shopping.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.dao.FakeCartProductDao
import woowacourse.shopping.fixture.model.PRODUCT_FIXTURE

class CartRepositoryImplTest {
    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        val cartProductDao = FakeCartProductDao()
        cartRepository = CartRepositoryImpl(cartProductDao)
    }

    @Test
    fun `장바구니 상품 추가 테스트`() =
        runTest {
            // when
            cartRepository.addCartProduct(PRODUCT_FIXTURE)

            // then
            val cartProducts = cartRepository.getAllCartProducts()
            assertThat(cartProducts).hasSize(1)
            assertThat(cartProducts.last().name).isEqualTo(PRODUCT_FIXTURE.name)
        }

    @Test
    fun `장바구니 상품 삭제 테스트`() =
        runTest {
            // given
            cartRepository.addCartProduct(PRODUCT_FIXTURE)

            // when
            cartRepository.deleteCartProduct(0)

            // then
            val cartProducts = cartRepository.getAllCartProducts()
            assertThat(cartProducts).isEmpty()
        }
}
