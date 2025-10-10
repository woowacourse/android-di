package woowacourse.shopping.fixture

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = listOf(PRODUCT_1L_A_1000, PRODUCT_2L_B_2000)
}
