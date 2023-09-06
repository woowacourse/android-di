package woowacourse.shopping.data

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository(private val cartProducts: MutableList<Product> = mutableListOf()) :
    CartRepository {
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.map { it.copy() }
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
