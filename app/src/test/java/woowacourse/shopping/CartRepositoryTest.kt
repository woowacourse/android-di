package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartRepositoryTest {
    private lateinit var repository: CartRepository

    @Before
    fun setup() {
        repository = DefaultCartRepository()
    }

    @Test
    fun `어떤 상품도 담지 않은 카트는 비어있다`() {
        // given

        // when
        val actual = repository.getAllCartProducts()

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `카트에 상품을 추가하면 카트에 상품이 추가된다`() {
        // given

        // when
        val product = Product("글로", 1000000000, "")
        repository.addCartProduct(product)

        // then
        assertThat(repository.getAllCartProducts()).contains(product)
    }

    @Test
    fun `카트에 상품이 추가된 상태에서 동일한 상품을 삭제하면 카트에서 상품이 삭제된다`() {
        // given
        val product = Product("글로", 1000000000, "")
        repository.addCartProduct(product)
        assertThat(repository.getAllCartProducts()).contains(product)

        // when
        repository.deleteCartProduct(0)

        // then
        assertThat(repository.getAllCartProducts()).doesNotContain(product)
    }
}
