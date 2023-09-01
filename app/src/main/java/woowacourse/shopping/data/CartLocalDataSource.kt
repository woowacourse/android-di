package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class CartLocalDataSource {
    private val _cartProducts: MutableList<Product> = mutableListOf()
    val cartProducts: List<Product>
        get() = _cartProducts.map { it.copy() }

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
