package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartFixture

class DefaultFakeCartRepository : CartRepository {
    private val carts: MutableList<CartProductEntity> = CartFixture.carts.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        carts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = carts.map { it.toCartProduct() }

    override suspend fun deleteCartProduct(id: Long) {
        carts.removeIf { it.id == id }
    }
}
