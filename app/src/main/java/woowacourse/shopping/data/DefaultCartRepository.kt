package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProductEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository
    @Inject
    constructor(private val dao: CartProductDao) : CartRepository {
        override suspend fun addCartProduct(product: Product) {
            dao.insert(product.toCartProductEntity())
        }

        override suspend fun getAllCartProducts(): List<CartProduct> {
            return dao.getAll().map { it.toModel() }
        }

        override suspend fun deleteCartProduct(id: Long) {
            dao.delete(id)
        }
    }
