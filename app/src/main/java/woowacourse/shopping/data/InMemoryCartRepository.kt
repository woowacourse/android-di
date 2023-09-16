package woowacourse.shopping.data

import com.lope.di.annotation.InMemoryMode
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@InMemoryMode
class InMemoryCartRepository : CartRepository {

    private val cartProducts: MutableList<CartProduct> = mutableListOf(
        CartProduct(
            0,
            "우테코 껌",
            2300,
            "",
            System.currentTimeMillis(),
        ),
    )

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                cartProducts.size.toLong(),
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
        val cartProduct = cartProducts.find { it.id == id }
        cartProducts.remove(cartProduct)
    }
}
