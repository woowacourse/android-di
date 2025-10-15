package woowacourse.shopping.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.domain.model.CartProduct

@Entity(tableName = "cart_products")
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun CartProductEntity.toDomain(): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
