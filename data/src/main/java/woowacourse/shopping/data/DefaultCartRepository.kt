package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.Database
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

@Database
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) = dao.insert(product.toEntity())

    override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) = dao.delete(id)
}
