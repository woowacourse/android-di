package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val products: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        products.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = products.toList()

    override suspend fun deleteCartProduct(id: Long) {
        products.removeAt(id.toInt())
    }
}
