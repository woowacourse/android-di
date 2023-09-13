package woowacourse.shopping.data

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@InMemory
class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<CartProduct>()
    override suspend fun addCartProduct(product: Product) {
        products.add(
            CartProduct(
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
        products.removeAt(id.toInt())
    }
}
