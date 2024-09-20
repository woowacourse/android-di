package woowacourse.shopping.model

data class Product(
    val id: Long = 0,
    val createdAt: Long = 0,
    val name: String,
    val price: Int,
    val imageUrl: String,
)
