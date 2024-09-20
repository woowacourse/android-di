package woowacourse.shopping.data.mapper

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.local.entity.CartProductEntity

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
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
