package woowacourse.shopping.fixture.repository

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.ProductRepository

class FakeProductRepository(
    private val products: List<Product> = listOf(),
) : ProductRepository {
    override fun getAllProducts(): List<Product> = products
}
