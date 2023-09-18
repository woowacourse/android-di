package woowacourse.shopping.data.repository

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import java.util.UUID

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf(
        CartProduct(
            id = UUID.randomUUID(),
            name = "반달",
            price = 60000000,
            imageUrl = "https://avatars.githubusercontent.com/u/22425650?v=4",
            createdAt = System.currentTimeMillis(),
        ),
    )

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartProduct(UUID.randomUUID()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts
    }

    override suspend fun deleteCartProduct(id: UUID) {
        cartProducts.removeIf { it.id == id }
    }
}
