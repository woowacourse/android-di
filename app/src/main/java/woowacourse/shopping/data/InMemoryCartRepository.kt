package woowacourse.shopping.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    private var nextId = 1L

    override suspend fun addCartProduct(product: Product) {
        val cartProduct =
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
        cartProducts.update { it + cartProduct }
    }

    override fun getAllCartProducts(): Flow<List<CartProduct>> = cartProducts.asStateFlow()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.update { products -> products.filterNot { it.id == id } }
    }
}
