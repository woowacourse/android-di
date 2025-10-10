package woowacourse.shopping.data.repository

import woowacourse.shopping.data.inmemory.CartProductEntity
import woowacourse.shopping.data.inmemory.toDomain
import woowacourse.shopping.data.mapper.toInMemoryEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toInMemoryEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
