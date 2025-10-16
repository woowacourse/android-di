package woowacourse.shopping.data.mapper

import com.example.domain.model.CartProduct
import com.example.domain.model.Product
import woowacourse.shopping.data.CartProductEntity

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun CartProductEntity.toDomain(): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
