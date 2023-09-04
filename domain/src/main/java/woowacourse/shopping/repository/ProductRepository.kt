package woowacourse.shopping.repository

import woowacourse.shopping.model.Product

interface ProductRepository {

    suspend fun getAllProducts(): List<Product>
}
