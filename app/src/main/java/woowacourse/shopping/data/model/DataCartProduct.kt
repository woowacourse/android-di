package woowacourse.shopping.data.model

import java.time.LocalDateTime

data class DataCartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: LocalDateTime,
)

