package woowacourse.shopping.data.repository

import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

@InMemory
class InMemoryCartRepository : CartRepository {

    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
