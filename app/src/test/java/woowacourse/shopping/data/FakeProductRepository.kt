package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    private val products: List<Product> =
        listOf(
            Product(1L, "Product1", 1000, "image1"),
            Product(2L, "Product2", 2000, "image2"),
            Product(3L, "Product3", 3000, "image3"),
        )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
