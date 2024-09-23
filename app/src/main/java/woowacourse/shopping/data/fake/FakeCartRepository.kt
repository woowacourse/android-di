package woowacourse.shopping.data.fake

import woowacourse.shopping.data.CartProductEntity

class FakeCartRepository : ICartRepository {
    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun addCartProduct(product: CartProductEntity) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return cartProducts
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAll { it.id == id }
    }
}
