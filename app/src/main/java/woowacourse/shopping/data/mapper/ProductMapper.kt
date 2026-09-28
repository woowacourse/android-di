package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import kotlin.random.Random

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun Product.toCartProduct(): CartProduct =
    CartProduct(
        id = Random.nextLong(10000000),
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = 0L,
    )
