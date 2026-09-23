package woowacourse.shopping.data

class FakeCartProductDao : CartProductDao {
    override suspend fun getAll(): List<CartProductEntity> = emptyList()
    override suspend fun insert(cartProduct: CartProductEntity) {}
    override suspend fun delete(id: Long) {}
}
