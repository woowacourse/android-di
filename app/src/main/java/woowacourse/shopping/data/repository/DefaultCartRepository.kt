package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Inject

class DefaultCartRepository
    @Inject
    constructor(
        private val cartProductDao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: Product) {
            cartProductDao.insert(product.toEntity())
        }

        override suspend fun getAllCartProducts(): List<Product> = cartProductDao.getAll().map { it.toProduct() }

        override suspend fun deleteCartProduct(id: Int) {
            cartProductDao.delete(id.toLong())
        }
    }
