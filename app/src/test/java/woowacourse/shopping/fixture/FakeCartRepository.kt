package woowacourse.shopping.fixture

import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val fakeCartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        fakeCartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = fakeCartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        fakeCartProducts.removeAt(id.toInt())
    }
}
