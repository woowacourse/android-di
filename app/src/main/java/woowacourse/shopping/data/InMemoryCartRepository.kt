package woowacourse.shopping.data

import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<Product>()

    override suspend fun addCartProduct(product: Product) {
        products.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return products
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }
}