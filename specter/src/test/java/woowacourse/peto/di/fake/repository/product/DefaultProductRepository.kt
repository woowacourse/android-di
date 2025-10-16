package woowacourse.peto.di.fake.repository.product

import woowacourse.peto.di.fake.model.Product

class DefaultProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
