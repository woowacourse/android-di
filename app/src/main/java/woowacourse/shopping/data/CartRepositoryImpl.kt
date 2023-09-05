package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartRepositoryImpl(
    private val cartProducts: MutableList<Product> = mutableListOf(),
) : CartRepository {
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }
}
