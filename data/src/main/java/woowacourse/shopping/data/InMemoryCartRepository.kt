package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.model.Product
import javax.inject.Inject


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

private const val TAG = "InMemoryCartRepository"
