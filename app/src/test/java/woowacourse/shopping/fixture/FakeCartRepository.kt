package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val products: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        products.add(product)
    }

    override fun getAllCartProducts(): List<Product> = products.toList()

    override fun deleteCartProduct(id: Int) {
        products.removeAt(id)
    }
}
