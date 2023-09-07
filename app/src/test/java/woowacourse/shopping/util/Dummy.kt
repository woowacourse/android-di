package woowacourse.shopping.util

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

object Dummy {

    val product = Product(
        name = "Inez Raymond",
        price = 1591,
        imageUrl = "https://www.google.com/#q=inceptos",
    )

    val cartProducts = listOf(
        CartProductEntity(
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ).apply { id = 0 },
        CartProductEntity(
            name = "우테코 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
        ).apply { id = 1 },
    )

    val products = listOf(
        Product(
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        Product(
            name = "우테코 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
        ),
    )
}
