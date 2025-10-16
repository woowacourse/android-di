package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
