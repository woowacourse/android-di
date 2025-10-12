package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProductEntity.toProduct(): Product =
    Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
