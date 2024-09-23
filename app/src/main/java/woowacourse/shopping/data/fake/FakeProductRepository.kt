package woowacourse.shopping.data.fake

import woowacourse.shopping.model.Product

class FakeProductRepository : IProductRepository {
    private val products = listOf(
        Product(
            name = "Product A",
            price = 3000,
            imageUrl = "https://example.com/image.jpg",
            createdAt = 10000,
        ),
        Product(
            name = "Product B",
            price = 2000,
            imageUrl = "https://example.com/image.jpg",
            createdAt = 10000,
        ),
    )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
