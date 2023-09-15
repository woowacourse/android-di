package woowacourse.shopping.model

data class CartProduct(
    val cartProductId: Long,
    val product: Product,
    val createdAt: Long,
)
