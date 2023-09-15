package woowacourse.shopping.data.repository

import com.bandal.di.Database
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toCartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import java.util.UUID

@Database
class DatabaseCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toCartProductEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: UUID) {
        dao.delete(id)
    }
}
