package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.repository.ProductDefaultRepository

class ProductDefaultRepositoryTest {
    private lateinit var productRepository: ProductDefaultRepository

    @Before
    fun setUp() {
        productRepository = ProductDefaultRepository()
    }

    @Test
    fun `전체 상품 조회 테스트`() {
        // when
        val products = productRepository.getAllProducts()

        // then
        assertThat(products).hasSize(3)
    }
}
