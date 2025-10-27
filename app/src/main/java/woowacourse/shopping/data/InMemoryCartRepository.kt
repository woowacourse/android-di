package woowacourse.shopping.data

import com.example.di.annotation.AppScoped
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

@AppScoped
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
