package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cartProducts = mutableListOf<Product>()

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
