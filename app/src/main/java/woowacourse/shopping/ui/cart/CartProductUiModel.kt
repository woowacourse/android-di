package woowacourse.shopping.ui.cart

data class CartProductUiModel(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val formattedDate: String,
)
