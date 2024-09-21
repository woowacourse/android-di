package woowacourse.shopping.data

import com.example.di.annotation.Singleton
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

@Singleton
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val id = cartProducts.size.toLong()
        cartProducts.add(product.toCartProduct(id))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        val newCartProducts = cartProducts.filterNot { it.id == id }
        cartProducts.clear()
        cartProducts.addAll(newCartProducts)
    }
}
