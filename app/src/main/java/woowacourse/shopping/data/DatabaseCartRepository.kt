package woowacourse.shopping.data

import com.ki960213.sheath.annotation.Repository
import woowacourse.shopping.data.mapper.toData
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
@Repository
class DatabaseCartRepository(private val dao: CartProductDao) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        dao.insert(CartProductEntity.from(product))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map(CartProductEntity::toData)
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
