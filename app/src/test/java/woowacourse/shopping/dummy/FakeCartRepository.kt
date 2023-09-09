package woowacourse.shopping.dummy

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val cartProducts = createFakeCartProducts().toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(CartProduct(product.name, product.price, product.imageUrl, 1))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
