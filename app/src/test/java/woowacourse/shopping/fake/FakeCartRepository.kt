package woowacourse.shopping.fake

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    val carts: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        carts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = carts

    override fun deleteCartProduct(id: Int) {
        carts.removeAt(id)
    }
}
