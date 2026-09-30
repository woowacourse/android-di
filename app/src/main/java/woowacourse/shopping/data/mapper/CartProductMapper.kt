package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun CartProductEntity.toDomain(): CartProduct =
    CartProduct(
        id = id, // TODO: 수정 해야할수도?
        product = Product(name, price, imageUrl),
        createdAt = createdAt,
    )
