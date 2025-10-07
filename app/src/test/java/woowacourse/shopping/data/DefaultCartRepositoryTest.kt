package woowacourse.shopping.data

import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

class DefaultCartRepositoryTest {

    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = DefaultCartRepository()
    }

    @Test
    fun `상품을 장바구니에 추가하면 목록에 포함된다`() {
        // given
        val product = Product(name = "우테코 과자", price = 10_000, imageUrl = "url")

        // when
        cartRepository.addCartProduct(product)

        // then
        val products = cartRepository.getAllCartProducts()
        assertThat(products).containsExactly(product)
    }

    @Test
    fun `상품을 삭제하면 목록에서 제거된다`() {
        // given
        val product = Product("우테코 과자", 10_000, "url1")
        cartRepository.addCartProduct(product)

        // when
        cartRepository.deleteCartProduct(0)

        // then
        val products = cartRepository.getAllCartProducts()
        assertThat(products).doesNotContain(product)
    }
}
