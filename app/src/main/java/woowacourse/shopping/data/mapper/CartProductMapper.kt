package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun CartProductEntity.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
}

fun List<CartProductEntity>.toProducts(): List<Product> {
    return map { it.toProduct() }
}
