package woowacourse.shopping

import woowacourse.shopping.domain.Product
import woowacourse.shopping.domain.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products: MutableList<Product> = mutableListOf()

    fun setProducts(fakeProducts: List<Product>) {
        products.addAll(fakeProducts)
    }

    override fun getAllProducts(): List<Product> = products
}
