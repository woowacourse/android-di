package woowacourse.shopping.data

class InMemoryCartProductDao : CartProductDao {
    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun getAll(): List<CartProductEntity> {
        return cartProducts
    }

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProducts.add(cartProduct)
    }

    override suspend fun delete(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
