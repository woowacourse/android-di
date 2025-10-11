package woowacourse.shopping.di.fake.repository.product

import woowacourse.shopping.di.fake.Product

class NotRegisteredProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
