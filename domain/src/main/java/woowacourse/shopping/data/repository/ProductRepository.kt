package woowacourse.shopping.data.repository

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}