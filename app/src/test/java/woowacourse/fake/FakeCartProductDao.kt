package woowacourse.fake

import woowacourse.fixture.CART_PRODUCTS_ENTITIES_FIXTURE
import woowacourse.shopping.data.db.CartProductDao
import woowacourse.shopping.data.db.CartProductEntity

class FakeCartProductDao : CartProductDao {
    private val cartProducts = CART_PRODUCTS_ENTITIES_FIXTURE.toMutableList()

    override suspend fun getAll(): List<CartProductEntity> = cartProducts.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
