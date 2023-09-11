package woowacourse.shopping.data.model.mapper

import woowacourse.shopping.data.model.DataCartProduct
import woowacourse.shopping.model.CartProduct

fun CartProduct.toData(): DataCartProduct =
    DataCartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )

fun DataCartProduct.toDomain(): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
