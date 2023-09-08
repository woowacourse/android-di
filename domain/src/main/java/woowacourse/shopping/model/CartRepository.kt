package woowacourse.shopping.model

interface CartRepository {

    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    fun deleteCartProduct(id: Int)
}
