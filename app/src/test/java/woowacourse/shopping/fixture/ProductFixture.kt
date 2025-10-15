package woowacourse.shopping.fixture

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

val TONKATSU = Product("돈가스", 11000, "image_url")
val TONKATSU_CART = CartProduct(TONKATSU, 2)

val MALATANG = Product("마라탕", 14000, "image_url")
val MALATANG_CART = CartProduct(MALATANG, 2)

val GOBCHANG = Product("곱창", 40000, "image_url")
val GOBCHANG_CART = CartProduct(GOBCHANG, 2)
