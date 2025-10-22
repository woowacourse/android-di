package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class PersistentCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }

    companion object {
        const val QUALIFIER = "persistent"
    }
}
