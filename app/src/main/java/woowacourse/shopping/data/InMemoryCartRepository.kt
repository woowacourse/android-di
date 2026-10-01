package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<CartProduct>()
    private var nextId = 1L

    override suspend fun addCartProduct(product: Product) {
        products +=
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = products.toList()

    override suspend fun deleteCartProduct(id: Long) {
        products.removeAll { it.id == id }
    }
}
