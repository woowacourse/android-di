package woowacourse.shopping.fixture

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = listOf(PRODUCT_1, PRODUCT_2, PRODUCT_3)
}
