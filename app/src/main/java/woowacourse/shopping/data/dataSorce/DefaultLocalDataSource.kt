package woowacourse.shopping.data.dataSorce

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultLocalDataSource(
    private val dao: CartProductDao,
) : LocalDataSource {
    override suspend fun getAll(): List<CartProduct> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun insert(product: Product) {
        return dao.insert(product.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }
}
