package woowacourse.shopping.ui

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products: List<Product> = listOf(
        Product(
            name = "우테코 과자1",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        Product(
            name = "우테코 과자2",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
    )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
