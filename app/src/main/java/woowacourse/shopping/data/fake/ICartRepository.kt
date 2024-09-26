package woowacourse.shopping.data.fake

import woowacourse.shopping.data.CartProductEntity

interface ICartRepository {
    suspend fun addCartProduct(product: CartProductEntity)
    suspend fun getAllCartProducts(): List<CartProductEntity>
    suspend fun deleteCartProduct(id: Long)
}
