package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.getProducts
import woowacourse.shopping.repository.ProductRepository

class ProductRepositoryTest {
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        repository = DefaultProductRepository()
    }

    @Test
    fun `모든 상품을 조회하면 모든 상품을 가져올 수 있다`() {
        // given

        // when
        val actual = repository.getAllProducts()

        // then
        val expected = getProducts()
        assertThat(actual).isEqualTo(expected)
    }
}
