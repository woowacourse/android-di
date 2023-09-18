package woowacourse.shopping.model

import java.time.LocalDateTime

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: LocalDateTime,
)
