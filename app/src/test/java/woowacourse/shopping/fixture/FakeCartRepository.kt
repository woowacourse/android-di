package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository(
    initialProducts: List<Product> = emptyList(),
) : CartRepository {
    private val cartProducts: MutableList<Product> = initialProducts.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { product -> product.id == id }
    }
}
