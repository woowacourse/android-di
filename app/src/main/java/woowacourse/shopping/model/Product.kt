package woowacourse.shopping.model

class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = CREATE_NONE,
) {
    companion object {
        private const val CREATE_NONE = -1L
    }
}
