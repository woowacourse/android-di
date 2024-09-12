package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product

class DefaultCartRepository(private val dao: CartProductDao) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun cartProducts(): List<CartProductEntity> =
        dao.getAll()

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
        // 여기서 id는 recyclerview의 position임
        Log.d("DefaultCartRepository", "deleteCartProduct: $id")
    }

    companion object{
        const val QUALIFIER_NAME = "RoomDB"
    }
}
