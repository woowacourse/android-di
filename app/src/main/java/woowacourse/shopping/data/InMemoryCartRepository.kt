package woowacourse.shopping.data

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableMap<Long, CartProduct> = linkedMapOf()
    private var nextId: Long = 1L

    override suspend fun addCartProduct(product: Product) {
        val id = nextId++
        cartProducts[id] =
            CartProduct(
                id = id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.values.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.remove(id)
    }
}
