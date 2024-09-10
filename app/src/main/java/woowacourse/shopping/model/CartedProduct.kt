package woowacourse.shopping.model

data class CartedProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long
)
