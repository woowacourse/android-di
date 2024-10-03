package woowacourse.shopping.data

import woowacourse.shopping.data.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}