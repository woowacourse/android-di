package woowacourse.shopping.model

class Product(
    val id: Int = 0,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = 0L,
)
