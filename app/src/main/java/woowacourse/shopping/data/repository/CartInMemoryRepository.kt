package woowacourse.shopping.data.repository

import com.medandro.di.annotation.Qualifier
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

@Qualifier("InMemory")
class CartInMemoryRepository : CartRepository {
    private val cartItems = mutableListOf<CartProductEntity>()
    private var currentId = 1L

    override suspend fun addCartProduct(product: Product) {
        val cartEntity =
            product.toEntity().apply {
                id = currentId++
                createdAt = System.currentTimeMillis()
            }

        cartItems.add(cartEntity)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartItems.map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Int) {
        cartItems.removeAll { it.id == id.toLong() }
    }
}
