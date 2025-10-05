package woowacourse.fixture

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.ProductRepository

class FakeProductRepository(
    private val fakeAllProducts: List<Product>,
) : ProductRepository {
    override fun getAllProducts(): List<Product> = fakeAllProducts
}
