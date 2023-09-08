package woowacourse.shopping.data

import woowacourse.shopping.di.Inject
import woowacourse.shopping.model.Product

class DefaultProductRepository : ProductRepository {
    @Inject
    private val products: List<Product> = emptyList()

    override fun getAllProducts(): List<Product> {
        return products
    }
}
