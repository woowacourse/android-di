package woowacourse.shopping.model

data class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
    var createdAt: Long = -1L,
)
