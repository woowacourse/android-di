package woowacourse.shopping.fixture

import woowacourse.shopping.model.Product

val ProductFixture =
    Product(
        id = 1,
        name = "우테코 과자",
        price = 10_000,
        imageUrl = "",
    )

val ProductsFixture =
    listOf(
        Product(1, "우테코 과자", 10_000, ""),
        Product(2, "우테코 쥬스", 8_000, ""),
        Product(3, "우테코 아이스크림", 20_000, ""),
    )
