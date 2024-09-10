package woowacourse.shopping.data.mapper

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.local.entity.CartProductEntity

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun CartProductEntity.toDomain(): Product {
    return Product(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}