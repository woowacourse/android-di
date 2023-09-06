package woowacourse.shopping.model

data class CartProduct(
    val identifier: DatabaseIdentifier,
    val product: Product,
    val createdAt: Long
)
