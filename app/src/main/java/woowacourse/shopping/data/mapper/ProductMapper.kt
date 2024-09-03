package woowacourse.shopping.data.mapper

import woowacourse.shopping.local.entity.CartProductEntity
import woowacourse.shopping.domain.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}
