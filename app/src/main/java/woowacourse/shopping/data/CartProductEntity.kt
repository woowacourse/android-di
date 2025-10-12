package woowacourse.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.model.Product
import java.time.LocalDateTime

@Entity(tableName = "cart_products")
data class CartProductEntity(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)