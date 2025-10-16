package woowacourse.shopping.ui

import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = listOf(DEFAULT_PRODUCT)
}
