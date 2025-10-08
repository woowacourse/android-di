package woowacourse.shopping.domain.repository

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
