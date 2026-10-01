package woowacourse.shopping.data

import kotlinx.coroutines.flow.Flow
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    fun getAllCartProducts(): Flow<List<CartProduct>>

    suspend fun deleteCartProduct(id: Long)
}
