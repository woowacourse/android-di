package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository(
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : CartRepository {
    private val cartProducts = mutableListOf<CartProduct>()
    private var nextId: Long = 1L

    override suspend fun addCartProduct(product: Product) {
        cartProducts +=
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = currentTimeMillis(),
            )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAll { cartProduct ->
            cartProduct.id == id
        }
    }
}
