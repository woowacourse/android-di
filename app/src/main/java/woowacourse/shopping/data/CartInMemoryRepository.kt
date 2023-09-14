package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartInMemoryRepository(
    cartProducts: List<CartProduct> = emptyList(),
) : CartRepository {
    private val cartProducts = cartProducts.toMutableList()

    private var id: Int = 0
        get() = field++

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartProduct())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }

    private fun Product.toCartProduct(): CartProduct {
        return CartProduct(
            name = name,
            price = price,
            imageUrl = imageUrl,
            createdAt = System.currentTimeMillis(),
            id = id,
        )
    }
}
