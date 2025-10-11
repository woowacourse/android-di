package woowacourse.shopping.di.fake.repository.product

import woowacourse.shopping.di.fake.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
