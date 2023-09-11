package woowacourse.shopping.model

data class CartProduct(
    val id: Long,
    val product: Product,
    val createdAt: Long,
)
