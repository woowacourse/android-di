package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<CartProduct>()
    override suspend fun addCartProduct(product: Product) {
        products.add(
            CartProduct(
                products.size.toLong(),
                product.name,
                product.price,
                product.imageUrl,
                System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return products.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }
}
