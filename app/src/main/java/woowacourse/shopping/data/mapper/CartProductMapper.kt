package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        id = id,
        createdAt = createdAt,
        product = Product(
            name = name,
            price = price,
            imageUrl = imageUrl,
        ),
    )
}
