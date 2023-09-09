package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.common.di.qualifier.DatabaseCartRepositoryQualifier
import woowacourse.shopping.ui.common.di.qualifier.InMemoryCartRepositoryQualifier

@DatabaseCartRepositoryQualifier
class DatabaseCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().toDomain()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

@InMemoryCartRepositoryQualifier
class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toDomain()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
