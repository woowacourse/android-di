package woowacourse.shopping.di.fake.repository.product

import woowacourse.shopping.di.fake.Product

class DefaultProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = emptyList()
}
