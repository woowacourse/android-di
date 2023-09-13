package woowacourse.shopping.model

class CartProduct(
    val name: String,
    val price: Int,
    val imageUrl: String,
    var id: Long,
    var createdAt: Long,
)
