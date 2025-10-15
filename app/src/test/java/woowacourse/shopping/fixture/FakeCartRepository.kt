package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.CartRepository

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

    fun setCartProducts(products: List<CartProductEntity>) {
        cartProducts.clear()
        cartProducts.addAll(products)
    }

    override suspend fun addCartProduct(product: CartProductEntity) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        if (id in cartProducts.indices) {
            cartProducts.removeAt(id.toInt())
        }
    }
}
