package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2
import woowacourse.shopping.fixture.PRODUCT_3
import woowacourse.shopping.model.Product

class DefaultCartRepositoryTest {
    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // given
        val product = Product("Test product", 1, "Test product image URL")
        val repository = DefaultCartRepository()

        // when
        repository.addCartProduct(product)

        // then
        val expected: List<Product> = listOf(product)
        val actual: List<Product> = repository.getAllCartProducts()
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `장바구니에서 특정 인덱스에 있는 상품을 제거할 수 있다`() {
        // given
        val repository =
            DefaultCartRepository().apply {
                addCartProduct(PRODUCT_1)
                addCartProduct(PRODUCT_2)
                addCartProduct(PRODUCT_3)
            }

        // when
        repository.deleteCartProduct(1)

        // then
        val expected: List<Product> = listOf(PRODUCT_1, PRODUCT_3)
        val actual: List<Product> = repository.getAllCartProducts()
        assertThat(expected).isEqualTo(actual)
    }
}
