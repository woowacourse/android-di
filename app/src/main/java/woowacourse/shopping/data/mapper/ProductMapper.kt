package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.db.CartProductEntity
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.data.inmemory.CartProductEntity as InMemoryEntity

fun Product.toEntity(): CartProductEntity {
    return CartProductEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}

fun Product.toInMemoryEntity(): InMemoryEntity {
    return InMemoryEntity(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}
