package woowacourse.shopping.data.repository

import com.bandal.di.Database
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@com.bandal.di.Database
class DatabaseCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
