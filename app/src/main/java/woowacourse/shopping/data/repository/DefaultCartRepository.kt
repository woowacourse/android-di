package woowacourse.shopping.data.repository

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

@Singleton
class DefaultCartRepository
    @Inject
    constructor(
        private val dao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: Product) = dao.insert(CartProductEntity.Companion.fromDomain(product))

        override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map(CartProductEntity::toDomain)

        override suspend fun deleteCartProduct(id: Long) = dao.delete(id.toLong())
    }
