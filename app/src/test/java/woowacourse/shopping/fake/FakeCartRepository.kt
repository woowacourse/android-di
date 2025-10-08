package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.POTATO
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf(POTATO)

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
