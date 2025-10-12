package woowacourse.shopping.model

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)