package woowacourse.shopping.data

import woowacourse.shopping.data.di.Inject
import woowacourse.shopping.data.di.RoomDB
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
@RoomDB
class RoomDBCartRepository(@Inject private val dao: CartProductDao) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toModel() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
