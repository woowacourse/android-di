package woowacourse.shopping.data

import com.m6z1.moongdi.annotation.Single
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.RoomDB
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@Single
@RoomDB
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
