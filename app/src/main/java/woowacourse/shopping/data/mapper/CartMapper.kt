package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun CartProductEntity.toDomain(): Product =
    Product(
        name,
        price,
        imageUrl,
        LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
    )

fun Product.toData() =
    CartProductEntity(
        name,
        price,
        imageUrl,
    )
