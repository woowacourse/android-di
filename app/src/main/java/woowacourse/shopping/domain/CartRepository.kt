package woowacourse.shopping.domain

import woowacourse.shopping.data.CartProductEntity

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProductEntity>

    suspend fun deleteCartProduct(id: Long)
}
