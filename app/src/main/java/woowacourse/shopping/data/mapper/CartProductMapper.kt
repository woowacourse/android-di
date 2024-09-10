package woowacourse.shopping.data.mapper

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.local.entity.CartProductEntity

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
}
