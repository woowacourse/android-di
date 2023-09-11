package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartInMemoryRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    private var id = 0L

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                cartProductId = id++,
                product = product,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
