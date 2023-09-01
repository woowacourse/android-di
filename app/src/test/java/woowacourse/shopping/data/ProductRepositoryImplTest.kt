package woowacourse.shopping.data

import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.util.Dummy

class ProductRepositoryImplTest {
    private val productRepository = ProductRepositoryImpl()

    @Test
    fun `모든 상품을 반환한다`() {
        // when
        val actual = productRepository.getAllProducts()
        // then
        val expected = Dummy.products
        assertEquals(expected, actual)
    }
}
