package woowacourse.peto.di.fake.repository.cart

import woowacourse.peto.di.fake.Product

class DefaultCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) {}

    override suspend fun getAllCartProducts(): List<Product> = emptyList()

    override suspend fun deleteCartProduct(id: Long) {}
}
