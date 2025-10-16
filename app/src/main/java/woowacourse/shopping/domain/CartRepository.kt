package woowacourse.shopping.domain

import woowacourse.shopping.data.CartProductEntity

interface CartRepository {
    suspend fun addCartProduct(product: CartProductEntity)

    suspend fun getAllCartProducts(): List<CartProductEntity>

    suspend fun deleteCartProduct(id: Long)
}
