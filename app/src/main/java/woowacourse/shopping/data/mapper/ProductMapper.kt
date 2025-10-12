package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun Product.toCartEntity() = CartProductEntity(name, price, imageUrl)