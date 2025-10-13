package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.GOBCHANG
import woowacourse.shopping.fixture.MALATANG
import woowacourse.shopping.fixture.TONKATSU
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cartProducts = mutableListOf<Product>(TONKATSU, MALATANG, GOBCHANG)

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList().map { CartProduct(it, 2) }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
