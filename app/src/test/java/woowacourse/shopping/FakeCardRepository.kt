package woowacourse.shopping

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCardRepository(
    intialProducts: List<CartProduct>,
) : CartRepository {
    private val cartProducts: MutableList<CartProduct> = intialProducts.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                id = cartProducts.size.toLong(),
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
