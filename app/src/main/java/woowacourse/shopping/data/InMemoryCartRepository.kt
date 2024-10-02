package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.model.Product
import javax.inject.Inject

// TODO: Step2 - CartProductDao를 참조하도록 변경
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

interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): List<Product>
    suspend fun deleteCartProduct(id: Int)
}

private const val TAG = "InMemoryCartRepository"
