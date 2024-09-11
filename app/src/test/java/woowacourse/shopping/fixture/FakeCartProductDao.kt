package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

object FakeCartProductDao : CartProductDao {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun getAll(): List<CartProductEntity> {
        return cartProducts.toList()
    }

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
