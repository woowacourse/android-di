package woowacourse.shopping.dummy

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val cartProducts = products.toMutableList()
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
