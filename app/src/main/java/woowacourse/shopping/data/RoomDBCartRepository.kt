package woowacourse.shopping.data

import com.hyegyeong.di.annotations.Inject
import woowacourse.shopping.data.di.Database
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@Database
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
