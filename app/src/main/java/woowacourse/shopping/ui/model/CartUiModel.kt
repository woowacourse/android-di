package woowacourse.shopping.ui.model

import woowacourse.shopping.domain.Product

data class CartUiModel(
    val id: Long,
    val product: Product,
    val createdAt: Long,
)
