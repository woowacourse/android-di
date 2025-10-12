package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2
import woowacourse.shopping.fixture.PRODUCT_3
import woowacourse.shopping.model.Product

class DatabaseCartRepositoryTest {
    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // given
        val product = Product("Test product", 1, "Test product image URL")
        val repository = DatabaseCartRepository()

        // when
        repository.addCartProduct(product)

        // then
        val actual: List<Product> = repository.getAllCartProducts()
        val expected: List<Product> = listOf(product)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `장바구니에서 특정 인덱스에 있는 상품을 제거할 수 있다`() {
        // given
        val repository =
            DatabaseCartRepository().apply {
                addCartProduct(PRODUCT_1)
                addCartProduct(PRODUCT_2)
                addCartProduct(PRODUCT_3)
            }

        // when
        repository.deleteCartProduct(1)

        // then
        val actual: List<Product> = repository.getAllCartProducts()
        val expected: List<Product> = listOf(PRODUCT_1, PRODUCT_3)
        assertThat(actual).isEqualTo(expected)
    }
}
