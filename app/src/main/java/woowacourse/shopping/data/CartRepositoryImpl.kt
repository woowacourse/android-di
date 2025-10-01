package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface CartRepository {
    fun addCartProduct(product: Product)

    fun getAllCartProducts(): List<Product>

    fun deleteCartProduct(id: Int)
}

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartRepositoryImpl : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
