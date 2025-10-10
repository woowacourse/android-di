package woowacourse.shopping.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.domain.model.Product

@Entity(tableName = "cart_products")
data class CartProductEntity(val name: String, val price: Int, val imageUrl: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var createdAt: Long = System.currentTimeMillis()
}

fun CartProductEntity.toDomain(): Product {
    return Product(name, price, imageUrl, createdAt)
}
