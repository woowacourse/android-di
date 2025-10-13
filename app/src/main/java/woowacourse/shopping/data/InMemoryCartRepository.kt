package woowacourse.shopping.data

import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
