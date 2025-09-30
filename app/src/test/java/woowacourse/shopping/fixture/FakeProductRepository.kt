package woowacourse.shopping.fixture

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository(
    initialProducts: List<Product> = emptyList(),
) : ProductRepository {
    private val products: List<Product> = initialProducts

    override fun getAllProducts(): List<Product> = products
}
