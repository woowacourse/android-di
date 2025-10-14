package woowacourse.shopping.di.fixture

import woowacourse.shopping.di.InMemory
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

@InMemory
class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
