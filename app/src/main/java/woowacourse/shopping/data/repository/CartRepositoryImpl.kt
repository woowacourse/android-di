package woowacourse.shopping.data.repository

import woowacourse.shopping.data.localStorage.CartProductDao
import woowacourse.shopping.data.model.mapper.toData
import woowacourse.shopping.data.model.mapper.toDomain
import woowacourse.shopping.data.model.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartRepositoryImpl(private val dao: CartProductDao) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toData().toDomain() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}
