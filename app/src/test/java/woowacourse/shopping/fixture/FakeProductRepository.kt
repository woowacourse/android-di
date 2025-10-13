package woowacourse.shopping.fixture

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = listOf(Product("Fake Product", 1000, ""))
}
