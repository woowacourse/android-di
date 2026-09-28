package woowacourse.shopping.data

import kotlinx.coroutines.delay
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository(
    initialProducts: List<CartProduct> = emptyList(),
) : CartRepository {
    val products = initialProducts.toMutableList()
    private var nextId = (initialProducts.maxOfOrNull { it.id } ?: 0L) + 1L

    override suspend fun addCartProduct(product: Product) {
        delay(1)
        products.add(
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = 1_700_000_000_000L,
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        delay(1)
        return products.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        delay(1)
        products.removeAll { it.id == id }
    }
}
