package woowacourse.shopping.data

import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.FakeProductRepository
import woowacourse.shopping.model.Product

class ProductRepositoryTest {

    @Test
    fun 저장된_모든_상품을_가져온다() {
        // given
        val products = listOf(Product("", 0, ""))
        val productRepository = FakeProductRepository(products)

        // when
        val actual = productRepository.getAllProducts()

        // then
        assertEquals(products, actual)
    }
}
