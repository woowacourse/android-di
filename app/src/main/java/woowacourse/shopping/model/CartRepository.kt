package woowacourse.shopping.model

import com.woowacourse.di.Singleton

@Singleton
interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
