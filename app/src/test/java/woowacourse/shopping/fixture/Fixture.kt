package woowacourse.shopping.fixture

import woowacourse.shopping.model.Product

val ProductFixture =
    Product(
        name = "우테코 과자",
        price = 10_000,
        imageUrl = "",
    )

val ProductsFixture =
    listOf(
        Product("우테코 과자", 10_000, ""),
        Product("우테코 쥬스", 8_000, ""),
        Product("우테코 아이스크림", 20_000, ""),
    )
