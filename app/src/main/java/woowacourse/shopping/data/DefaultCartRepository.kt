package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.DatabaseIdentifier
import woowacourse.shopping.model.Identifier
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val cartProductDao: CartProductDao
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProductDao.getAll().map {
            it.toDomain()
        }
    }

    override suspend fun deleteCartProduct(id: Identifier<*>) {
        if (id is DatabaseIdentifier)
            cartProductDao.delete(id.value)
    }
}
