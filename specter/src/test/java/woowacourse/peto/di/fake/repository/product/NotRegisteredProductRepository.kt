package woowacourse.peto.di.fake.repository.product

import woowacourse.peto.di.fake.Product

class NotRegisteredProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
