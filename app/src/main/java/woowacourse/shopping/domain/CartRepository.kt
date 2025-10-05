package woowacourse.shopping.domain

import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
