package woowacourse.fake

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.domain.model.Product

class FakeProductRepository : ProductRepository {
    private val products =
        mutableListOf<Product>()

    override fun getAllProducts(): List<Product> {
    }
}
