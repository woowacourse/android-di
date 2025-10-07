package woowacourse.shopping.fake

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository(
    private val products: List<Product> = emptyList(),
) : ProductRepository {
    override fun getAllProducts(): List<Product> = products
}
