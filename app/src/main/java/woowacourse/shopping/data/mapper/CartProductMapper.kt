package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct

fun CartProductEntity.toCartProduct(): CartProduct {
    return CartProduct(name, price, imageUrl, createdAt)
}
