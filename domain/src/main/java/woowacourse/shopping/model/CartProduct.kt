package woowacourse.shopping.model

data class CartProduct(
    val identifier: Identifier<*>,
    val product: Product,
    val createdAt: Long
)
