package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): List<CartProduct>
    suspend fun deleteCartProduct(id: Long)
}
