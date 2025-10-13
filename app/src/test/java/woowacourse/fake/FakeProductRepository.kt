package woowacourse.fake

import woowacourse.fixture.PRODUCTS_FIXTURE
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.domain.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> = PRODUCTS_FIXTURE
}
