package woowacourse.shopping.fixture

import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = productsFixture
}
