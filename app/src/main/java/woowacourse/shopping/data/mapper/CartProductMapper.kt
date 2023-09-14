package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.Product

fun CartProductEntity.toProduct(): Product {
    return Product(name, price, imageUrl, createdAt)
}