package woowacourse.shopping.fixture.dao

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class FakeCartProductDao(
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf(),
) : CartProductDao {
    override suspend fun getAll(): List<CartProductEntity> = cartProducts.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
