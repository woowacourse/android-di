package woowacourse.shopping

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

val fakeProducts: List<Product> =
    listOf(
        Product(
            name = "테스트 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        Product(
            name = "테스트 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
        ),
        Product(
            name = "테스트 아이스크림",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
        ),
    )

val fakeCartProducts: List<CartProduct> =
    listOf(
        CartProduct(
            name = "테스트 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
            id = 1,
            createdAt = System.currentTimeMillis(),
        ),
        CartProduct(
            name = "테스트 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
            id = 2,
            createdAt = System.currentTimeMillis(),
        ),
        CartProduct(
            name = "테스트 아이스크림",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
            id = 3,
            createdAt = System.currentTimeMillis(),
        ),
    )
