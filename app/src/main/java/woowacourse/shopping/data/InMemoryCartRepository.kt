package woowacourse.shopping.data

import com.kmlibs.supplin.annotations.Supply
import woowacourse.shopping.di.InMemoryRepository
import woowacourse.shopping.model.CartedProduct
import woowacourse.shopping.model.Product

@InMemoryRepository
class InMemoryCartRepository @Supply constructor() : CartRepository {
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
