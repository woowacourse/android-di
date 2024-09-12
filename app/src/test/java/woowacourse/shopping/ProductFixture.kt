package woowacourse.shopping

import woowacourse.shopping.model.Product

fun ProductFixture(
    id: Long = 1,
    name: String = "product $id",
    price: Int = 1_000 * id.toInt(),
    imageUrl: String = "image $id",
    createdAt: Long = 10_000 * id,
) = Product(id, name, price, imageUrl, createdAt)
