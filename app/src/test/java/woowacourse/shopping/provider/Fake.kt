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

    override fun addCartProduct(product: Product) {
        val cartProduct = CartProduct(0, product, 0L)
        products.add(cartProduct)
    }

    override fun getAllCartProducts(): List<CartProduct> {
        return products.toList()
    }

    override fun deleteCartProduct(id: Int) {
        products.removeAt(id)
    }
}
