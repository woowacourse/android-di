package woowacourse.shopping

import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository(val products: List<Product> = emptyList()) : ProductRepository {
    override fun getAllProducts(): List<Product> = products
}
