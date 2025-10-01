package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.Product

interface ProductRepository {
    val products: List<Product>

    fun getAllProducts(): List<Product>
}
