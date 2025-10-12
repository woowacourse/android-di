package woowacourse.peto.di.fake.repository.cart

import woowacourse.peto.di.fake.model.CartProduct
import woowacourse.peto.di.fake.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}
