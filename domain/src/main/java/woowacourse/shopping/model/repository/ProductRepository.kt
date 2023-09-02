package woowacourse.shopping.model.repository

import woowacourse.shopping.model.Product

interface ProductRepository {

    fun getAllProducts(): List<Product>
}
