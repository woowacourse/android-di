package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product

class CartRepositoryImpl(
    private val cartProductDao: CartProductDao
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProductDao.getAll().map(CartProductEntity::toProduct)
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
