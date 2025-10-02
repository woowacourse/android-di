package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}
