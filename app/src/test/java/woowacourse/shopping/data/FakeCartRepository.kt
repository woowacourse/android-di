package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class FakeCartRepository(
    private val cartProducts: MutableList<Product> = mutableListOf(),
) : CartRepository {
    constructor(vararg products: Product) : this(products.toMutableList())

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun allCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        val product = cartProducts.find { it.id == id }
        cartProducts.remove(product)
    }
}
