package woowacourse.shopping.ui

import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val products: MutableList<Product> = mutableListOf(DEFAULT_PRODUCT)

    override suspend fun addCartProduct(product: Product) {
        products.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = products.toList()

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }
}
