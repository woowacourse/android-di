package woowacourse.shopping.model

data class Product(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)
