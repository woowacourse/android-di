package woowacourse.shopping.data.model.mapper

import woowacourse.shopping.data.model.DataProduct
import woowacourse.shopping.model.Product

fun Product.toData(): DataProduct =
    DataProduct(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )

fun DataProduct.toDomain(): Product =
    Product(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
