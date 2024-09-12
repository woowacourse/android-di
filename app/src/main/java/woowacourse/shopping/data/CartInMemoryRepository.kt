package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class CartInMemoryRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    private var id = 0L

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(CartProduct(id++, product, System.currentTimeMillis()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
