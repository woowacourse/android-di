package woowacourse.shopping.data

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<CartProduct>()
    override suspend fun addCartProduct(product: Product) {
        products.add(
            CartProduct(
                getNewProductId(),
                product.name,
                product.price,
                product.imageUrl,
                System.currentTimeMillis(),
            ),
        )
    }

    private fun getNewProductId(): Long {
        return if (products.isNotEmpty()) {
            products.maxOf { it.id }.toLong() + 1
        } else {
            DEFAULT_ID
        }
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return products.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }

    companion object {
        private const val DEFAULT_ID = 1L
    }
}
