package woowacourse.shopping.domain

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: CartProduct)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Int)
}
