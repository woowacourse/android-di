package woowacourse.shopping.data

import com.ki960213.sheath.annotation.Repository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import java.time.LocalDateTime

@Repository
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    private var nextId: Long = 0

    override suspend fun addCartProduct(product: Product) {
        val cartProduct = CartProduct(
            id = ++nextId,
            product = product,
            createdAt = LocalDateTime.now(),
        )
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
