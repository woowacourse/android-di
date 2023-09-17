package woowacourse.shopping.data.datasource

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface CartDataSource {

    suspend fun addCartProduct(product: Product): Long

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
