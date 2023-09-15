package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct

fun CartProductEntity.toCartProduct(): CartProduct = CartProduct(
    id = id,
    createdAt = createdAt,
    name = name,
    price = price,
    imageUrl = imageUrl,
)
