package woowacourse.shopping.data

import com.ki960213.sheath.annotation.Repository
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@Repository
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> = mutableListOf()
    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
