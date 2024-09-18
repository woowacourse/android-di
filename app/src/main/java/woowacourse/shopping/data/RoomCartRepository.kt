package woowacourse.shopping.data

import olive.di.annotation.Singleton
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

@Singleton
class RoomCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return dao.getAll()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
