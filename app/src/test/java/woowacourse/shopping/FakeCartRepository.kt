package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository(products: List<Product> = emptyList()) : CartRepository {

    private val _products: MutableList<Product> = products.toMutableList()

    val products: List<Product>
        get() = _products.toList()

    override fun addCartProduct(product: Product) {
        _products.add(product)
    }

    override fun getAllCartProducts(): List<Product> =
        products.also {
            println(it)
        }

    override fun deleteCartProduct(id: Int) {
        _products.removeAt(id)
    }
}
