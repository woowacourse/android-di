package woowacourse.shopping.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.domain.model.Product

@Entity(tableName = "cart_products")
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    var createdAt: Long = System.currentTimeMillis()
}

fun CartProductEntity.toDomain(): Product = Product(id, name, price, imageUrl, createdAt)
