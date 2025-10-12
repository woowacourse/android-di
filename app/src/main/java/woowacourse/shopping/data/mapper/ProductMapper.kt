package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.Product
import woowacourse.shopping.ui.model.CartUiModel

fun Product.toEntity(): CartProductEntity =
    CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = System.currentTimeMillis(),
    )

fun CartProductEntity.toDomain(): Product =
    Product(
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
    )

fun CartProductEntity.toPresentation(): CartUiModel =
    CartUiModel(
        id = this.id,
        product = this.toDomain(),
        createdAt = this.createdAt,
    )
