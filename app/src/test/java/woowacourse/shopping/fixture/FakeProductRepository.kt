package woowacourse.shopping.fixture

import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = productsFixture
}
