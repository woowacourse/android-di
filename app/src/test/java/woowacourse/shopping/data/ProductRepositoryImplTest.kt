package woowacourse.shopping.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {
    private lateinit var productRepository: ProductRepositoryImpl

    @Before
    fun setUp() {
        productRepository = ProductRepositoryImpl()
    }

    @Test
    fun `전체 상품 조회 테스트`() {
        // when
        val products = productRepository.getAllProducts()

        // then
        assertThat(products).hasSize(3)
    }
}
