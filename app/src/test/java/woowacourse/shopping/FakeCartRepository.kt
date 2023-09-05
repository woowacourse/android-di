package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository(products: List<CartProduct> = emptyList()) : CartRepository {

    private val _products: MutableList<CartProduct> = products.toMutableList()

    val products: List<CartProduct>
        get() = _products.toList()

    override suspend fun addCartProduct(product: Product) {
        _products.add(CartProduct(product, 0L))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = products

    override suspend fun deleteCartProduct(id: Long) {
        _products.removeAt(id.toInt())
    }
}
