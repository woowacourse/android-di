package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        id = this.id,
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt,
    )
}
