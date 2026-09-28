package woowacourse.shopping.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val mutex = Mutex()
    private val cartProducts = mutableListOf<CartProduct>()
    private var nextId = 1L

    override suspend fun addCartProduct(product: Product) {
        mutex.withLock {
            cartProducts.add(
                CartProduct(
                    id = nextId++,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    createdAt = System.currentTimeMillis(),
                ),
            )
        }
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = mutex.withLock { cartProducts.toList() }

    override suspend fun deleteCartProduct(id: Long) {
        mutex.withLock {
            cartProducts.removeAll { it.id == id }
        }
    }
}
