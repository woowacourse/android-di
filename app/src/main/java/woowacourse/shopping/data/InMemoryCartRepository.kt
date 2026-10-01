package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    private var id = 0L

    override suspend fun addCartProduct(product: Product) {
        cartProducts +=
            CartProduct(
                id = id++,
                createdAt = System.currentTimeMillis(),
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
            )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAll { it.id == id }
    }
}
