package woowacourse.shopping.fixture.repository

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class FakeCartRepository(
    private val cartProducts: MutableList<Product> = mutableListOf(),
) : CartRepository {
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
