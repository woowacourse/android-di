package woowacourse.shopping.model

class CartProduct(
    val id: Long,
    val createdAt: Long,
    product: Product,
) {
    val name = product.name
    val price = product.price
    val imageUrl = product.imageUrl
}
