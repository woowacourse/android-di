package woowacourse.shopping.data.datasource

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartDataSource(
    private val dao: CartProductDao,
) : CartDataSource {

    override suspend fun addCartProduct(product: Product): Long {
        return dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
