package woowacourse.shopping.main

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.TEST_PRODUCTS

class ProductRepositoryImplTest {
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        productRepository = ProductRepositoryImpl()
    }

    @Test
    fun `모든 상품들을 조회할 수 있다`() {
        // given

        // when
        val actual = productRepository.getAllProducts()

        // then
        Assert.assertEquals(TEST_PRODUCTS, actual)
    }
}
