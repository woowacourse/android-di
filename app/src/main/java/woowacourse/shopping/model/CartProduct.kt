package woowacourse.shopping.model

data class CartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)

fun Product.toCart(id: Long): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = System.currentTimeMillis(),
    )
