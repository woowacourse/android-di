package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class FakeCartRepositoryOnDisk(private val dao: CartProductDao) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return dao.getAll()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }

    companion object {
        @Volatile
        private var instance: FakeCartRepositoryOnDisk? = null

        fun getInstance(dao: CartProductDao): FakeCartRepositoryOnDisk {
            return instance ?: synchronized(this) {
                instance ?: FakeCartRepositoryOnDisk(dao).also { instance = it }
            }
        }
    }
}
