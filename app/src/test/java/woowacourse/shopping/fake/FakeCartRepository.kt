package woowacourse.shopping.fake

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository(initialCart: List<Product>) : CartRepository {
    private val cart = mutableListOf<Product>().apply { addAll(initialCart) }

    override suspend fun addCartProduct(product: Product) {
        cart.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cart.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cart.removeAt(id.toInt())
    }
}
