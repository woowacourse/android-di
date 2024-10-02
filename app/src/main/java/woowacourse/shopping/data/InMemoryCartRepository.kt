package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Step2 - CartProductDao를 참조하도록 변경
@Singleton
class InMemoryCartRepository @Inject constructor() : CartRepository {

    init {
        Log.d(TAG, "init InMemoryCartRepository created")
    }

    private val cartProducts: MutableList<Product> = mutableListOf()
    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}

@Singleton
class LocalCartRepository @Inject constructor(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id.toLong())
    }
}


interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): List<Product>
    suspend fun deleteCartProduct(id: Int)
}

private const val TAG = "InMemoryCartRepository"
