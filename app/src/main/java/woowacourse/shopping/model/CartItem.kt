package woowacourse.shopping.model

data class CartItem(
    val id: Long,
    val product: Product,
    val createdAt: Long,
)
