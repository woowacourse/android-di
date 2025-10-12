package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class FakeCartProductDao : CartProductDao {
    private val cartProducts = mutableListOf<CartProductEntity>()
    private var autoIncrementId = 1L

    override suspend fun getAll(): List<CartProductEntity> = cartProducts.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        if (cartProduct.id == 0L) {
            cartProduct.id = autoIncrementId++
        }
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
