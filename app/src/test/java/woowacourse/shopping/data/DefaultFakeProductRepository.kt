package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.main.ProductFixture

class DefaultFakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> =
        ProductFixture.products
}
