package woowacourse.shopping.fixture.repository

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class FakeCartRepository(
    private val cartProducts: MutableList<CartProduct> = mutableListOf(),
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        val cartProduct =
            CartProduct(
                id = cartProducts.size.toLong(),
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
