package woowacourse.shopping.repository

import kotlinx.coroutines.flow.Flow
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): Flow<List<Product>>
    suspend fun deleteCartProduct(id: Long)
}
