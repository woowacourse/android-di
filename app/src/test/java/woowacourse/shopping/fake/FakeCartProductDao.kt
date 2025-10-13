package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class FakeCartProductDao : CartProductDao {
    private val products = mutableListOf<CartProductEntity>()

    override suspend fun getAll(): List<CartProductEntity> = products.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        products.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        products.removeIf { it.id == id }
    }
}
