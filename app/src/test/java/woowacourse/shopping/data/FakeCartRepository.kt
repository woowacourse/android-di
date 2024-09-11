package woowacourse.shopping.data

import woowacourse.shopping.model.CartedProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private var products: List<CartedProduct> = emptyList()

    override suspend fun addCartProduct(product: Product) {
        products = products +
            CartedProduct(
                product.id,
                product.name,
                product.price,
                product.imageUrl,
                System.currentTimeMillis(),
            )
    }

    override suspend fun getAllCartProducts(): List<CartedProduct> = products

    override suspend fun deleteCartProduct(id: Long) {
        products = products.filter { it.id != id }
    }
}
