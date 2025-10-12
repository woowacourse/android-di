package woowacourse.shopping.data

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) = dao.insert(CartProductEntity.fromDomain(product))

    override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Int) = dao.delete(id.toLong())
}
