package woowacourse.shopping.hilt.data.mapper

import woowacourse.shopping.hilt.data.CartProductEntity
import woowacourse.shopping.hilt.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}
