package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// todo: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    override fun addCartProduct(product: Product) {
        // todo Dao로 변경하며 createdAt 부분 수정
        val cartProduct = CartProduct(0, product, 0L)
        cartProducts.add(cartProduct)
    }

    override fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
