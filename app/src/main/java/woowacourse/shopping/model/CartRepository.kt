package woowacourse.shopping.model

import com.example.di.annotation.Singleton

@Singleton
interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
