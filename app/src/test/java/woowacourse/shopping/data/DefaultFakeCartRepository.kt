package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartFixture

class DefaultFakeCartRepository : CartRepository {
    private val carts: MutableList<Product> = CartFixture.carts.toMutableList()

    override fun addCartProduct(product: Product) {
        carts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = carts

    override fun deleteCartProduct(id: Int) {
    }
}
