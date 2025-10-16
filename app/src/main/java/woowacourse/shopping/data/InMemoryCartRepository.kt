package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val store = mutableListOf<CartProduct>()

    override suspend fun addCartProduct(product: Product) {
        val nextId = (store.maxOfOrNull { it.id } ?: 0L) + 1
        store.add(CartProduct(nextId, product.name, product.price, product.imageUrl, System.currentTimeMillis()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = store.toList()

    override suspend fun deleteCartProduct(id: Long) {
        store.removeIf { it.id == id }
    }
}
