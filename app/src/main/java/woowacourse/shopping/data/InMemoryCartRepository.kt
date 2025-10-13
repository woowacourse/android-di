package woowacourse.shopping.data

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

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
