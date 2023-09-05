package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}

fun CartProductEntity.toDomain(): CartProduct {
    return CartProduct(
        Product(
            name = name,
            price = price,
            imageUrl = imageUrl
        ),
        createdAt
    )
}
