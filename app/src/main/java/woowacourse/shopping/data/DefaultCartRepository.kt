package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.mapper.toCartProductEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository(private val dao: CartProductDao) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toCartProductEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toModel() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        Log.d("DefaultCartRepository", "deleteCartProduct: $id")
        dao.delete(id)
    }
}
