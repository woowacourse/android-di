package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.local.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

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
        createdAt = createdAt,
    )
}

fun Product.toCartProduct(id: Long): CartProduct {
    return CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = System.currentTimeMillis(),
    )
}
