package woowacourse.shopping.data

import woowacourse.shopping.di.annotation.InMemoryMode
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@InMemoryMode
class InMemoryCartRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf()
    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                0,
                product.name,
                product.price,
                product.imageUrl,
                System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
