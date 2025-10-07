package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.Product

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    fun setCartProducts(products: List<Product>) {
        cartProducts.clear()
        cartProducts.addAll(products)
    }

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        if (id in cartProducts.indices) {
            cartProducts.removeAt(id)
        }
    }
}
