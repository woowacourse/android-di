package woowacourse.shopping.fixture.repository

import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class FakeProductRepository(
    private val products: List<Product> = listOf(),
) : ProductRepository {
    override fun getAllProducts(): List<Product> = products
}
