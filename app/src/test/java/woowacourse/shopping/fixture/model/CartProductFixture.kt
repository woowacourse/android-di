package woowacourse.shopping.fixture.model

import woowacourse.shopping.domain.model.CartProduct

val CART_PRODUCT_FIXTURE =
    CartProduct(
        id = 0L,
        name = "우테코 과자",
        price = 10_000,
        imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        createdAt = System.currentTimeMillis(),
    )

val CART_PRODUCTS_FIXTURE =
    listOf(
        CART_PRODUCT_FIXTURE,
        CART_PRODUCT_FIXTURE.copy(id = 1L),
        CART_PRODUCT_FIXTURE.copy(id = 2L),
    )
