package woowacourse.shopping.di.fake.repository.cart

import woowacourse.shopping.di.fake.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
