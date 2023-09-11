package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
