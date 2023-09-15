package woowacourse.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "cart_products")
data class CartProductEntity(val name: String, val price: Int, val imageUrl: String) {

    @PrimaryKey
    var id: UUID = UUID.randomUUID()

    var createdAt: Long = System.currentTimeMillis()
}
