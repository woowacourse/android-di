package woowacourse.shopping.ui.model

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.Product
import woowacourse.shopping.domain.toDomain

data class CartUiModel(
    val id: Long,
    val product: Product,
    val createdAt: Long,
)

fun CartProductEntity.toPresentation(): CartUiModel =
    CartUiModel(
        id = this.id,
        product = this.toDomain(),
        createdAt = this.createdAt,
    )
