package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val fakeCartProducts: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        fakeCartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = fakeCartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        fakeCartProducts.removeAt(id)
    }
}
