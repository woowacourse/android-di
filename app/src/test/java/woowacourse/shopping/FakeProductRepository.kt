package woowacourse.shopping

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository(val products: List<Product>) : ProductRepository {
    override fun getAllProducts(): List<Product> {
        return products
    }
}