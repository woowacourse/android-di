package woowacourse.shopping.data

import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    private val products: MutableList<Product> =
        mutableListOf(
            Product(
                name = "콜라",
                price = 1000,
                imageUrl = "https://image.com",
            ),
            Product(
                name = "사이다",
                price = 1000,
                imageUrl = "https://image.com",
            ),
            Product(
                name = "포도주스",
                price = 2000,
                imageUrl = "https://image.com",
            ),
        )

    override fun getAllProducts(): List<Product> = products.toList()
}
