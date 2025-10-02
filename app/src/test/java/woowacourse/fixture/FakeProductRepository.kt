package woowacourse.fixture

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = listOf(PRODUCT_A_1000, PRODUCT_B_2000)
}
