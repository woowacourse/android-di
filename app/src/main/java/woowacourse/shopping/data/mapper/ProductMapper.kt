package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.model.Product

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProductEntity.toProduct(): Product =
    Product(
        id = id.toInt(),
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
