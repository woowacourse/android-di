package woowacourse.shopping.fake

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository(initialCart: List<Product>) : CartRepository {
    private val cart = mutableListOf<Product>().apply { addAll(initialCart) }

    override fun addCartProduct(product: Product) {
        cart.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cart.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cart.removeAt(id)
    }
}
