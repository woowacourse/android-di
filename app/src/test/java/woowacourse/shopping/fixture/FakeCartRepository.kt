package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val storage = mutableListOf<Product>()

    override fun addCartProduct(product: Product) {
        storage.add(product)
    }

    override fun getAllCartProducts(): List<Product> = storage.toList()

    override fun deleteCartProduct(id: Int) {
        storage.removeAt(id)
    }
}
