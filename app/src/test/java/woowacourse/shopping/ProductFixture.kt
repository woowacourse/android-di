package woowacourse.shopping

import woowacourse.shopping.model.Product

fun ProductFixture(
    id: Int = 1,
    name: String = "product $id",
    price: Int = 1000 * id,
    imageUrl: String = "image $id",
) = Product(name, price, imageUrl)
