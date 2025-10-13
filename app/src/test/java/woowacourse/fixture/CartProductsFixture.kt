package woowacourse.fixture

import woowacourse.shopping.data.db.CartProductEntity

val CART_PRODUCTS_ENTITIES_FIXTURE =
    listOf(
        CartProductEntity(
            id = 0,
            name = "사과",
            price = 3000,
            imageUrl = "",
        ),
        CartProductEntity(
            id = 1,
            name = "딸기",
            price = 3000,
            imageUrl = "",
        ),
    )
