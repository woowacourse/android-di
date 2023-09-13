package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun CartProductEntity.toCartProduct(): CartProduct {
    return CartProduct(
        name = name,
        price = price,
        imageUrl = imageUrl,
        id = id,
        createdAt = createdAt,
    )
}
