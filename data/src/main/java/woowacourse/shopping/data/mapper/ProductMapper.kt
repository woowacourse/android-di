package woowacourse.shopping.data.mapper

import woowacourse.shopping.local.CartProductEntity
import woowacourse.shopping.data.model.Product

fun Product.toEntity(): CartProductEntity = CartProductEntity(
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun CartProductEntity.toDomain(): Product = Product(
    name = name,
    price = price,
    imageUrl = imageUrl,
    createdAt = createdAt,
)