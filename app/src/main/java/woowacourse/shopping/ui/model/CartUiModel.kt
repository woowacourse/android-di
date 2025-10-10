package woowacourse.shopping.ui.model

import woowacourse.shopping.domain.Product

data class CartUiModel(
    val product: Product,
    val createdAt: Long,
)
