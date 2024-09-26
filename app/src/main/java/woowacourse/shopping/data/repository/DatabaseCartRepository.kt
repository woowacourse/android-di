package woowacourse.shopping.data.repository

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.local.dao.CartProductDao
import javax.inject.Inject

class DatabaseCartRepository
    @Inject
    constructor(
        private val dao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: Product) {
            dao.insert(product.toEntity())
        }

        override suspend fun getAllCartProducts(): List<CartProduct> {
            return dao.getAll().map { it.toDomain() }
        }

        override suspend fun deleteCartProduct(id: Long) {
            dao.delete(id)
        }
    }
