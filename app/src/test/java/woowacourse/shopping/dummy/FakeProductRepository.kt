package woowacourse.shopping.dummy

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> {
        return products
    }
}
