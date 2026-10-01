package woowacourse.shopping.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override fun getAllCartProducts(): Flow<List<CartProduct>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
