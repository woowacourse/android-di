package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.cart.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun CartProductEntity.toDomain(): CartProduct {
    val product = Product(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

    return CartProduct(
        id = id,
        product = product,
        createdAt = createdAt,
    )
}
