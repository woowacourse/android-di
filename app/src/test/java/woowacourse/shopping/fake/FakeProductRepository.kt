package woowacourse.shopping.fake

import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = PRODUCTS
}

val PRODUCTS =
    listOf(
        Product(
            name = "떡볶이",
            price = 2000,
            imageUrl = "",
        ),
        Product(
            name = "떡볶이2",
            price = 2000,
            imageUrl = "",
        ),
    )
