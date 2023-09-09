package woowacourse.shopping.model

data class CartProduct(
    val id: Long,
    val createdAt: Long,
    val product: Product,
) {
    val name = product.name
    val price = product.price
    val imageUrl = product.imageUrl
}
