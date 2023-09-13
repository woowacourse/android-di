package woowacourse.shopping.model

import java.util.UUID

data class CartProduct(
    val id: UUID,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)
