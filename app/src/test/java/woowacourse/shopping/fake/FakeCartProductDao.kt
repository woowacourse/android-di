package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class FakeCartProductDao : CartProductDao {
    private val products: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun getAll(): List<CartProductEntity> {
        return products
    }

    override suspend fun insert(cartProduct: CartProductEntity) {
        products.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        products.removeAt(id.toInt())
    }
}
