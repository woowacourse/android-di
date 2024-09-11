package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.Inject
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class CartRepositoryImpl(
    @Inject private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) = cartProductDao.insert(product.toEntity())

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProductDao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) = cartProductDao.delete(id)
}
