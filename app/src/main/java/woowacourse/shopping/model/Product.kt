package woowacourse.shopping.model

data class Product(
    val id: Long = -1L,
    val name: String,
    val price: Int,
    val imageUrl: String,
    var createdAt: Long = -1L,
)
