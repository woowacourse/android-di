package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DatabaseCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map(CartProductEntity::toCartProduct)

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
