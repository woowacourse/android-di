package woowacourse.shopping.data.inmemory

import woowacourse.shopping.domain.model.Product
import java.util.UUID

data class CartProductEntity(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
)

fun CartProductEntity.toDomain(): Product = Product(id, name, price, imageUrl, createdAt)
