package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                id = cartProducts.size.toLong(),
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
