package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.CartProduct

interface CartRepository {
    suspend fun addCartProduct(cartProduct: CartProduct)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
