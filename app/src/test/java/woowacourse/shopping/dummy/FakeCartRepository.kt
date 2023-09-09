package woowacourse.shopping.dummy

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val cartProducts = createFakeProducts().toMutableList()

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
