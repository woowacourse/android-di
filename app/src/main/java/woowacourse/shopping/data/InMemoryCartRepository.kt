package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProduct>()
    private var nextId = 1L

    override suspend fun addCartProduct(product: Product) {
        cartProducts +=
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAll { it.id == id }
    }
}
