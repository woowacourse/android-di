package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeRoomCartRepository(
    private val fakeCartProductDao: FakeCartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        fakeCartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = fakeCartProductDao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Long) {
        fakeCartProductDao.delete(id)
    }
}
