package woowacourse.shopping.model

import java.time.LocalDateTime

class Product(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: LocalDateTime = CREATE_NONE,
) {
    companion object {
        private val CREATE_NONE = LocalDateTime.now()
    }
}
