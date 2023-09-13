package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun Product.toEntity(): CartProductEntity = CartProductEntity(
    name = name,
    price = price,
    imageUrl = imageUrl,
)

fun List<CartProductEntity>.toDomain(): List<CartProduct> = map { cartProductEntity ->
    CartProduct(
        product = Product(
            name = cartProductEntity.name,
            price = cartProductEntity.price,
            imageUrl = cartProductEntity.imageUrl,
        ),
        id = cartProductEntity.id,
        createdAt = cartProductEntity.createdAt,
    )
}
