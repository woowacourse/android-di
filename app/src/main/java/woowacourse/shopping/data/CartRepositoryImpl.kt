package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toData
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.di.Database
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl : CartRepository {
    @Inject
    @Database
    private lateinit var dao: CartProductDao

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
