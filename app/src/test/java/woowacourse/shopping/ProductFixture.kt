package woowacourse.shopping

import woowacourse.shopping.model.Product

fun ProductFixture(
    id: Long = 1,
    name: String = "product $id",
    price: Int = 1000 * id.toInt(),
    imageUrl: String = "image $id",
    createdAt: Long = System.currentTimeMillis()
) = Product(id, name, price, imageUrl, createdAt)
