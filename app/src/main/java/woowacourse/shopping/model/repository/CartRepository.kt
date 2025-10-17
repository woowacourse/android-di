package woowacourse.shopping.model.repository

import com.example.di_v2.annotation.AppScoped
import woowacourse.shopping.model.Product

@AppScoped
interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
