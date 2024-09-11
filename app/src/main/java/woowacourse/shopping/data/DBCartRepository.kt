package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartedProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartedProduct
import woowacourse.shopping.model.Product

class DBCartRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartedProduct> {
        return cartProductDao.getAll().map(CartProductEntity::toCartedProduct)
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
