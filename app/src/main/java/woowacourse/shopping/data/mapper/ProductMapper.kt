package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
    )
}

fun CartProductEntity.toDomain(): Product {
    return Product(
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
    )
}
