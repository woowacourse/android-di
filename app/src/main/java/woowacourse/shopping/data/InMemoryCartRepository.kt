package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class InMemoryCartRepository(
    initialProducts: List<Product> = emptyList(),
) : CartRepository {
    private val cartProducts: MutableList<Product> = initialProducts.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
