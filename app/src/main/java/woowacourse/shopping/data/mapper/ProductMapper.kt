package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun List<CartProductEntity>.toDomain(): List<Product> {
    return map {
        Product(
            name = it.name,
            price = it.price,
            imageUrl = it.imageUrl,
        )
    }
}
