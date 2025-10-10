package woowacourse.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cart = mutableListOf<Product>()

    override suspend fun getAllCartProducts(): List<Product> = cart.toList()

    override suspend fun addCartProduct(product: Product) {
        cart.add(product)
    }

    override suspend fun deleteCartProduct(id: Long) {
        cart.removeAll { it.id == id }
    }
}
