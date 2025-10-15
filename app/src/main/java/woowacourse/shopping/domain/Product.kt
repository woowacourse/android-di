package woowacourse.shopping.domain

import woowacourse.shopping.data.CartProductEntity

data class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
)

fun Product.toData(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
