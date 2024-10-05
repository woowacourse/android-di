package woowacourse.shopping.hilt.fake

import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.model.Product
import javax.inject.Inject

class StubProductRepository @Inject constructor() : ProductRepository {
    override suspend fun allProducts(): List<Product> {
        return emptyList()
    }
}