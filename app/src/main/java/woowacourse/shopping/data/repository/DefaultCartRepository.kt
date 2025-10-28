package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.CartRepository
import javax.inject.Inject

class DefaultCartRepository
    @Inject
    constructor(
        private val dao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: CartProductEntity) = dao.insert(product)

        override suspend fun getAllCartProducts(): List<CartProductEntity> = dao.getAll()

        override suspend fun deleteCartProduct(id: Long) = dao.delete(id)
    }
