package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun allCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
