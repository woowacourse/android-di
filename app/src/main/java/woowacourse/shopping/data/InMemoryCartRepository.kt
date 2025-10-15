package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import java.util.UUID

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val cartProduct =
            CartProduct(
                id = UUID.randomUUID().mostSignificantBits,
                product = product,
                createdAt = System.currentTimeMillis(),
            )
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
