package woowacourse.shopping.domain

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
