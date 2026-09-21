package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class CartRepository(
    private val cartProductDao: CartProductDao,
) {

    suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    suspend fun getAllCartProducts(): List<CartProduct> =
        cartProductDao.getAll().map { it.toDomain() }

    suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
