package woowacourse.shopping.data

import com.android.di.annotation.Inject
import woowacourse.shopping.data.di.annotation.RoomDatabase
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class CartRepositoryImpl(
    @Inject
    @RoomDatabase
//    @InMemoryDatabase :: can use
    private val dao: CartProductDao,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}
