package woowacourse.shopping.di.fake.repository

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.domain.model.Product

class FakeNotRegisteredProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
