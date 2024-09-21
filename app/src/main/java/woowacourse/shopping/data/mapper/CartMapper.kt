package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun CartProductEntity.toDomain(): Product =
    Product(
        id,
        name,
        price,
        imageUrl,
        createdAt,
    )

fun Product.toData() =
    CartProductEntity(
        name,
        price,
        imageUrl,
    )
