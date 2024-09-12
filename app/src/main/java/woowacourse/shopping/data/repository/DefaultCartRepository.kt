package woowacourse.shopping.data.repository

import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.FieldInject
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class DefaultCartRepository : CartRepository {
    @property:FieldInject
    private lateinit var dao: CartProductDao

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
