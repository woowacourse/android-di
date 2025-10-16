package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.model.Product

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
