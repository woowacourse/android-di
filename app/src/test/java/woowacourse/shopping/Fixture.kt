package woowacourse.shopping

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.DatabaseIdentifier
import woowacourse.shopping.model.Identifier
import woowacourse.shopping.model.Product

fun CartProduct(
    identifier: Identifier<*> = DatabaseIdentifier(0),
    product: Product = Product(),
    createdAt: Long = 0
) = CartProduct(
    identifier, product, createdAt
)

fun Product(name: String = "", price: Int = 0, imageUrl: String = "") =
    Product(name, price, imageUrl)
