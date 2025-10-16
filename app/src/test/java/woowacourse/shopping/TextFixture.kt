package woowacourse.shopping

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product

val DEFAULT_PRODUCT = Product(name = "name", price = 1000, imageUrl = "")

val DEFAULT_CART_PRODUCT =
    CartProduct(
        id = 1L,
        name = "name",
        price = 1000,
        imageUrl = "",
    )
