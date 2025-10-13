package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product
import java.util.concurrent.atomic.AtomicLong

class InMemoryCartRepository(
    initialProducts: List<Product> = emptyList(),
) : CartRepository {
    private val idGenerator: AtomicLong = AtomicLong()
    private val cartProducts: MutableList<Product> = initialProducts.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.copy(id = idGenerator.incrementAndGet()))
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { product -> product.id == id }
    }
}
