package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartRepositoryImpl(cartProduct: List<Product> = listOf()) : CartRepository {
    private val _cartProducts: MutableList<Product> = cartProduct.toMutableList()
    override fun addCartProduct(product: Product) {
        _cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return _cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        _cartProducts.removeAt(id)
    }
}
