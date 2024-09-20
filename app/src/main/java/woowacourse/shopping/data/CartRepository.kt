package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProductEntity>

    suspend fun deleteCartProduct(id: Int)
}
