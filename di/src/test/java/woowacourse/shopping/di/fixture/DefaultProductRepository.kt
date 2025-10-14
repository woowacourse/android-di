package woowacourse.shopping.di.fixture

import woowacourse.shopping.di.Database
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

@Database
class DefaultProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
