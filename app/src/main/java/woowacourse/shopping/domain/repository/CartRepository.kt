package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
