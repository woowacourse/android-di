package woowacourse.shopping.data

import woowacourse.shopping.data.annotation.Qualifier
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@Qualifier("InMemoryCartRepository")
class InMemoryCartRepository() : CartRepository {
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
