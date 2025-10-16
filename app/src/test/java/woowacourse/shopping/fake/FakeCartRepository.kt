package woowacourse.shopping.fake

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.toDomain
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.GOBCHANG_CART
import woowacourse.shopping.fixture.MALATANG_CART
import woowacourse.shopping.fixture.TONKATSU_CART
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cartProducts =
        mutableListOf<CartProduct>(TONKATSU_CART, MALATANG_CART, GOBCHANG_CART)

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity().toDomain())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
