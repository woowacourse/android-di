package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
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
        val product1 = Product("Test product 1", 1, "Test product image URL 1")
        val product2 = Product("Test product 2", 2, "Test product image URL 2")
        val repository =
            DefaultCartRepository().apply {
                addCartProduct(product1)
                addCartProduct(product2)
            }

        // when
        repository.deleteCartProduct(0)

        // then
        val expected: List<Product> = listOf(product2)
        val actual: List<Product> = repository.getAllCartProducts()
        assertThat(expected).isEqualTo(actual)
    }
}
