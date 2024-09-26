package woowacourse.shopping.data

import com.woowacourse.di.annotations.Inject
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.toProduct
import woowacourse.shopping.shoppingapp.di.annotation.RoomDatabase

class CartRepositoryImpl(
    @Inject
    @RoomDatabase
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return dao.getAll().map { it.toProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
