package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: CartProductEntity) = dao.insert(product)

    override suspend fun getAllCartProducts(): List<CartProductEntity> = dao.getAll()

    override suspend fun deleteCartProduct(id: Long) = dao.delete(id)
}
