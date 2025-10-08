package woowacourse.shopping

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCardRepository(
    intialProducts: List<Product>,
) : CartRepository {
    private val cartProducts: MutableList<Product> = intialProducts.toMutableList()

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cartProducts

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
