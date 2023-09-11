package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class DefaultProductRepository : ProductRepository {

    override suspend fun getAllProducts(): List<Product> {
        return Dummy.products
    }
}
