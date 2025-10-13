package woowacourse.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import woowacourse.shopping.domain.model.Product

@Entity(tableName = "cart_products")
data class CartProductEntity(
    val name: String,
    val price: Int,
    val imageUrl: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var createdAt: Long = System.currentTimeMillis()

    fun toDomain(): Product =
        Product(
            id = id,
            name = name,
            price = price,
            imageUrl = imageUrl,
            createdAt = createdAt,
        )

    companion object {
        fun fromDomain(product: Product): CartProductEntity =
            CartProductEntity(
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
            )
    }
}
