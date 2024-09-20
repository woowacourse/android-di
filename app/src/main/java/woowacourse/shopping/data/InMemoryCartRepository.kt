package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toInMemoryCartProduct
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProduct>()

    override suspend fun addCartProduct(product: Product) {
        val cartProduct = product.toInMemoryCartProduct(id = cartProducts.size.toLong() + 1)
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
