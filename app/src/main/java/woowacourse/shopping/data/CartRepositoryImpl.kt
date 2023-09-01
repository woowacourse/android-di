package woowacourse.shopping.data

import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartRepositoryImpl(cartProduct: List<Product> = listOf()) {
    private val _cartProducts: MutableList<Product> = cartProduct.toMutableList()
    fun addCartProduct(product: Product) {
        _cartProducts.add(product)
    }

    fun getAllCartProducts(): List<Product> {
        return _cartProducts.toList()
    }

    fun deleteCartProduct(id: Int) {
        _cartProducts.removeAt(id)
    }
}
