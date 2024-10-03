package woowacourse.shopping.data

import woowacourse.shopping.data.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): List<Product>
    suspend fun deleteCartProduct(id: Int)
}
