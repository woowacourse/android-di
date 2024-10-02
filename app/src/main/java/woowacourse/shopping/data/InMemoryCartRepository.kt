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
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}

interface CartRepository {
    fun addCartProduct(product: Product)
    fun getAllCartProducts(): List<Product>
    fun deleteCartProduct(id: Int)
}

private const val TAG = "InMemoryCartRepository"
