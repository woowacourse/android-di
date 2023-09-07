package woowacourse.shopping.provider

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
    private val products: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        products.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return products.toList()
    }

    override fun deleteCartProduct(id: Int) {
        products.removeAt(id)
    }
}
