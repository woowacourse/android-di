package woowacourse.shopping.model

class CartProduct(
    val product: Product,
    val createdAt: Long = System.currentTimeMillis(),
)
