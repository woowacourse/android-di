package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    private val products: List<Product> =
        listOf(
            Product("Product1", 1000, "image1"),
            Product("Product2", 2000, "image2"),
            Product("Product3", 3000, "image3"),
        )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
