package woowacourse.shopping.model

import woowacourse.shopping.data.CartProductEntity

class Product(
    val id: Long = 0L,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)

fun CartProductEntity.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
}
