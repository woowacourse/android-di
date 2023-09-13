package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.DatabaseIdentifier
import woowacourse.shopping.model.Identifier
import woowacourse.shopping.model.Product

class FakeCartRepository(products: List<CartProduct> = emptyList()) : CartRepository {

    private val _products: MutableList<CartProduct> = products.toMutableList()
    private var id: Long = 0

    val products: List<CartProduct>
        get() = _products.toList()

    override suspend fun addCartProduct(product: Product) {
        _products.add(CartProduct(DatabaseIdentifier(id++), product, 0L))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = products
    override suspend fun deleteCartProduct(id: Identifier<*>) {
        if (!_products.removeIf { it.identifier == id })
            throw IndexOutOfBoundsException()
    }
}
