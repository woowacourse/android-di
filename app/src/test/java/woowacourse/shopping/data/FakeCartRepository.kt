package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class FakeCartRepository(
    private val cartProducts: MutableList<Product> = mutableListOf(),
) : CartRepository {
    constructor(vararg products: Product) : this(products.toMutableList())

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun allCartProducts(): List<Product> = cartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
