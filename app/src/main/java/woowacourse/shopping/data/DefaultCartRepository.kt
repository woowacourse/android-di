package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val dao: CartDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(CartProductEntity(product.name, product.price, product.imageUrl)).also {
            Log.d(TAG, "addCartProduct: $it")
        }
    }

    override suspend fun allCartProducts(): List<Product> = dao.getAll().toData()

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

fun List<CartProductEntity>.toData(): List<Product> =
    map {
        Product(it.id, it.name, it.price, it.imageUrl, it.createdAt)
    }

private const val TAG = "DefaultCartRepository"
