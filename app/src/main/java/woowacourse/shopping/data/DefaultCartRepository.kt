package woowacourse.shopping.data

import com.example.sh1mj1.Inject
import com.example.sh1mj1.Qualifier
import woowacourse.shopping.model.Product

class DefaultCartRepository : CartRepository {
    @Inject
    @Qualifier("RoomDao", generate = true)
    lateinit var dao: CartProductDao

    override suspend fun addCartProduct(product: Product) {
        dao.insert(CartProductEntity(product.name, product.price, product.imageUrl))
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
