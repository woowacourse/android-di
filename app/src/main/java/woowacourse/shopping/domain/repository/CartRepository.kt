package woowacourse.shopping.domain.repository

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProductEntity>

    suspend fun deleteCartProduct(id: Int)
}
