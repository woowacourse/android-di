package woowacourse.shopping.data.repository

import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class PersistentCartRepository(private val cartProductDao: CartProductDao) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProductDao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
