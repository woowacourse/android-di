package woowacourse.shopping.provider

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

internal object Fake {
    fun ProductRepository(): ProductRepository = FakeProductRepository()

    fun CartRepository(): CartRepository = FakeCartRepository()
}

private class FakeProductRepository : ProductRepository {
    private val products: MutableList<Product> = mutableListOf()

    override fun getAllProducts(): List<Product> {
        return products.toList()
    }
}

private class FakeCartRepository : CartRepository {
    private val products: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val cartProduct = CartProduct(id = 0L, product = product, createdAt = 0L)
        products.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return products.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeAt(id.toInt())
    }
}
