package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.model.Product

class ProductRepositoryTest {
    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given
        val repository = FakeProductRepository()

        // then
        val actual: List<Product> = repository.getAllProducts()
        val expected: List<Product> =
            listOf(
                Product("Product fixture 1", 1_000, "URL 1", 1),
                Product("Product fixture 2", 2_000, "URL 2", 2),
                Product("Product fixture 3", 3_000, "URL 3", 3),
            )
        assertThat(actual).isEqualTo(expected)
    }
}
