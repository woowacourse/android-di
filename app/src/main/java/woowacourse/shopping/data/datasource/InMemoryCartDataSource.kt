package woowacourse.shopping.data.datasource

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartDataSource : CartDataSource {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    private var lastIndex = 0L

    override suspend fun addCartProduct(product: Product): Long {
        cartProducts.add(CartProduct(lastIndex, System.currentTimeMillis(), product))
        return lastIndex++
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf {
            it.id == id
        }
    }
}
