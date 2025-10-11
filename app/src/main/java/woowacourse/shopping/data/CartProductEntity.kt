package woowacourse.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@Entity(tableName = "cart_products")
data class CartProductEntity(val name: String, val price: Int, val imageUrl: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var createdAt: Long = System.currentTimeMillis()
}

fun CartProductEntity.toDomain(): CartProduct =
    CartProduct(
        product = Product(name = name, price = price, imageUrl = imageUrl),
        createdAt = createdAt,
    )
