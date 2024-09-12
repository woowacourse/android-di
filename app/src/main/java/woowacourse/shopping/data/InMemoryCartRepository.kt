package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.model.Product

class InMemoryCartRepository(
    private val cartProducts: MutableList<Product> = mutableListOf(),
) : CartRepository {
    constructor(vararg products: Product) : this(products.toMutableList())

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product).also {
            Log.d(TAG, "addCartProduct: $product")
        }
    }

    override suspend fun allCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}

private const val TAG = "InMemoryCartRepository"
