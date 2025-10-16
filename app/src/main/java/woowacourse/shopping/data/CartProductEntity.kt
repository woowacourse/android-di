package woowacourse.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_products")
data class CartProductEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    var createdAt: Long = System.currentTimeMillis()
}
