package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    init {
        println("CartRepository 생성")
    }

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
