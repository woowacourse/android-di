package woowacourse.shopping.data.repository

import com.medandro.di.annotation.Qualifier
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

@Qualifier("InMemory")
class CartInMemoryRepository : CartRepository {
    private val cartItems = mutableListOf<CartProductEntity>()
    private var currentId = 1L

    override suspend fun addCartProduct(product: Product) {
        val cartEntity =
            CartProductEntity(
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
            ).apply {
                id = currentId++
                createdAt = System.currentTimeMillis()
            }

        cartItems.add(cartEntity)
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> = cartItems.toList()

    override suspend fun deleteCartProduct(id: Int) {
        cartItems.removeAll { it.id == id.toLong() }
    }
}
