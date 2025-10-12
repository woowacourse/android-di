package woowacourse.shopping.model

class Product(
    val id: Long? = null,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long? = null,
)
