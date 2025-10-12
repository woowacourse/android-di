package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
