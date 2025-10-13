package woowacourse.shopping.domain.model

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
)
