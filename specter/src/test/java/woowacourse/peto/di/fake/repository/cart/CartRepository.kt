package woowacourse.peto.di.fake.repository.cart

import woowacourse.peto.di.fake.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
