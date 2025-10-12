package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProductEntity.toDomain(): Product =
    Product(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )