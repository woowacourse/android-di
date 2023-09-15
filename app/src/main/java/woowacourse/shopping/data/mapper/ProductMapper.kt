package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import java.util.UUID

fun Product.toCartProductEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun Product.toCartProduct(): CartProduct = CartProduct(
    id = UUID.randomUUID(),
    name = name,
    price = price,
    imageUrl = imageUrl,
    createdAt = System.currentTimeMillis(),
)

fun CartProductEntity.toCartProduct(): CartProduct = CartProduct(
    id = UUID.randomUUID(),
    name = name,
    price = price,
    imageUrl = imageUrl,
    createdAt = createdAt,
)
