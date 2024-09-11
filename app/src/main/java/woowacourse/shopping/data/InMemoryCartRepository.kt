package woowacourse.shopping.data

import android.util.Log
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        Log.e("TEST", "InMemoryCartRepository addCartProduct")
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
