package woowacourse.shopping.data

import jakarta.inject.Inject
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class DefaultCartRepository @Inject constructor(
    private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> =
        cartProductDao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
