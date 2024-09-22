package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.local.CartProductEntity
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        addedAt = createdAt,
    )
}
