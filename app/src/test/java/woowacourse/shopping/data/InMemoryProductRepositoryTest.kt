package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class InMemoryProductRepositoryTest {
    private lateinit var productRepository: InMemoryProductRepository

    @Before
    fun setUp() {
        productRepository = InMemoryProductRepository()
    }

    @Test
    fun `전체 상품 조회`() {
        // when
        val products = productRepository.getAllProducts()

        // then
        assertThat(products).hasSize(3)
    }
}
