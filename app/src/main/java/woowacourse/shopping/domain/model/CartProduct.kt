package woowacourse.shopping.domain.model

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val addedAt: Long,
)