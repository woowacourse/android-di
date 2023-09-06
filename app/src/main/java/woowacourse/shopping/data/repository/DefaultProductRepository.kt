package woowacourse.shopping.data.repository

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.ProductRepository

class DefaultProductRepository : ProductRepository {

    private val products: List<Product> = listOf(
        Product(
            id = 1,
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        Product(
            id = 2,
            name = "우테코 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
        ),
        Product(
            id = 3,
            name = "우테코 아이스크림",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
        ),
        Product(
            id = 4,
            "갤럭시 z폴드 5",
            2_000_000,
            "https://images.samsung.com/kdp/goods/2023/07/11/f6d71ef1-2f1c-48fa-9d2c-22a544c160c5.png",
        ),
        Product(
            id = 5,
            "갤럭시 z플립 5",
            1_200_000,
            "https://images.samsung.com/kdp/goods/2023/07/11/2b8aed55-ac36-44b0-83f9-0562d8040686.png",
        ),
    )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
