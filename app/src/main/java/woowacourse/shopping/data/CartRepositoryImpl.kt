package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toData
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class CartRepositoryImpl(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toData())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}
