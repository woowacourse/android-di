package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val products: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        products.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> =
        products.mapIndexed { index: Int, product: Product ->
            CartProduct(
                id = index.toLong(),
                product = product,
                createdAt = index.toLong(),
            )
        }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeAt(id.toInt())
    }
}
