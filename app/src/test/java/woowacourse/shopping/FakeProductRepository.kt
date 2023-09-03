package woowacourse.shopping

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class FakeProductRepository(private val products: List<Product>) : ProductRepository {

    override fun getAllProducts(): List<Product> {
        return products.toList()
    }
}
