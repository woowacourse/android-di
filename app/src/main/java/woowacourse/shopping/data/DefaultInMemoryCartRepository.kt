package woowacourse.shopping.data

import com.example.domain.model.CartProduct
import com.example.domain.model.Product
import com.example.domain.repository.CartRepository

class DefaultInMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val cartProduct =
            CartProduct(
                id = System.currentTimeMillis(),
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts
    }

    override suspend fun deleteCartProduct(id: Long) {
        val cartProduct = cartProducts.find { it.id == id }
        cartProducts.remove(cartProduct)
    }
}
