package woowacourse.shopping.data

import woowacourse.shopping.model.CartedProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartedProduct>

    suspend fun deleteCartProduct(id: Long)
}
