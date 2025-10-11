package woowacourse.shopping.data.repository

import com.medandro.di.annotation.RoomDB
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

@RoomDB
class CartRoomRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> = dao.getAll()

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}
