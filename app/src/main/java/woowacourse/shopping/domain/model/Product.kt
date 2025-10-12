package woowacourse.shopping.domain.model

class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
)
