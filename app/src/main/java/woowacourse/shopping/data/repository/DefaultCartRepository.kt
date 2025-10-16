package woowacourse.shopping.data.repository

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.repository.CartRepository

@Singleton
class DefaultCartRepository
    @Inject
    constructor(
        private val dao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(cartProduct: CartProduct) =
            dao.insert(
                CartProductEntity.fromDomain(
                    cartProduct,
                ),
            )

        override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map(CartProductEntity::toDomain)

        override suspend fun deleteCartProduct(id: Long) = dao.delete(id)
    }
