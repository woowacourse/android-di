package woowacourse.shopping.repository

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartRepository {
    fun addCartProduct(product: Product)
    fun getAllCartProducts(): List<CartProduct>
    fun deleteCartProduct(id: Int)
}
