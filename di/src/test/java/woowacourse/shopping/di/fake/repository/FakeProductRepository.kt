package woowacourse.shopping.di.fake.repository

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.domain.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
