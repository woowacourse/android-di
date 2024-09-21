package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun cartProducts(): List<CartProductEntity>

    suspend fun deleteCartProduct(id: Long)
}
