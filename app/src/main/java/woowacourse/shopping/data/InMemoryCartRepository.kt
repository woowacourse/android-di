package woowacourse.shopping.data

import woowacourse.shopping.annotation.SingleInstance
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@SingleInstance
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val cartProduct = CartProduct(
            System.currentTimeMillis(),
            product.name,
            product.price,
            product.imageUrl,
            System.currentTimeMillis(),
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
