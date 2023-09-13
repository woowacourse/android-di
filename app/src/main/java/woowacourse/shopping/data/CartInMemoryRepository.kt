package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartInMemoryRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    private var id = 0L

    override suspend fun addCartProduct(product: Product) {
        val newCartProduct = CartProduct(
            id = id++,
            name = product.name,
            price = product.price,
            imageUrl = product.imageUrl,
            timeInCart = System.currentTimeMillis(),
        )
        cartProducts.add(newCartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
