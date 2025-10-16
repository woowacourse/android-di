package woowacourse.shopping.ui

import woowacourse.shopping.DEFAULT_CART_PRODUCT
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val products: MutableList<CartProduct> = mutableListOf(DEFAULT_CART_PRODUCT)

    override suspend fun addCartProduct(cartProduct: CartProduct) {
        products.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = products.toList()

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }
}
