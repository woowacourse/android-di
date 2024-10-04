package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.model.Product
import woowacourse.shopping.local.CartProductDao
import woowacourse.shopping.local.CartProductEntity

class LocalCartRepository(
    private val dao: CartProductDao,
) : CartRepository {

    init {
        Log.d(TAG, "init LocalCartRepository created")
    }

    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}

private const val TAG = "LocalCartRepository"
