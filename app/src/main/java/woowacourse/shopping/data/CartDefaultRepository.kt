package woowacourse.shopping.data

import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class CartDefaultRepository(
    private val cartProductDao: CartProductDao
) : CartRepository {

    private val cartProducts: MutableList<Product> = mutableListOf()
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
