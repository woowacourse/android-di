package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct

fun CartProductEntity.toCartProduct(): CartProduct {
    return CartProduct(id, name, price, imageUrl, createdAt)
}
