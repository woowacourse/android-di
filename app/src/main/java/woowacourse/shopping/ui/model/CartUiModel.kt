package woowacourse.shopping.ui.model

import woowacourse.shopping.data.CartProductEntity

data class CartUiModel(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)

fun CartProductEntity.toPresentation(): CartUiModel =
    CartUiModel(
        id = this.id,
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt,
    )
