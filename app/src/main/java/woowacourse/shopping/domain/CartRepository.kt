package woowacourse.shopping.domain

import com.zzang.di.annotation.lifecycle.ApplicationComponent
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@ApplicationComponent
interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
