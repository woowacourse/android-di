package woowacourse.shopping.model

data class CartProduct(
    val id: Long,
    val createdAt: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
)
