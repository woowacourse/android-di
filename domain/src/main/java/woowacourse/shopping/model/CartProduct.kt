package woowacourse.shopping.model

import java.time.LocalDateTime

data class CartProduct(
    val id: Long,
    val product: Product,
    val createdAt: LocalDateTime,
)
