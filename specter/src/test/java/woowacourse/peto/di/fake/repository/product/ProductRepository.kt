package woowacourse.peto.di.fake.repository.product

import woowacourse.peto.di.fake.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
