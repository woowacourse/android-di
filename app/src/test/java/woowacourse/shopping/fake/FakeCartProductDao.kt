package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class FakeCartProductDao : CartProductDao {

    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun getAll(): List<CartProductEntity> {
        return cartProducts.toList()
    }

    override suspend fun insert(cartProduct: CartProductEntity) {
        val existingIndex = cartProducts.indexOfFirst { it.id == cartProduct.id }
        if (existingIndex != -1) {
            cartProducts[existingIndex] = cartProduct
        } else {
            cartProducts.add(cartProduct)
        }
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeAll { it.id == id }
    }
}