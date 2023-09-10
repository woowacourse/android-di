package woowacourse.shopping.model

class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val timeInCart: Long = 0L,
)
