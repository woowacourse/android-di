package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        id,
        createdAt,
        Product(name, price, imageUrl),
    )
}
