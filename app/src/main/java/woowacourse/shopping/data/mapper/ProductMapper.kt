package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProduct.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun CartProductEntity.toModel(): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
