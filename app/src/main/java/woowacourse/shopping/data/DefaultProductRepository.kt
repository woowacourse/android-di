package woowacourse.shopping.data

import woowacourse.shopping.di.Inject
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class DefaultProductRepository : ProductRepository {

    @Inject
    lateinit var products: List<Product>

    override fun getAllProducts(): List<Product> {
        return products
    }
}
