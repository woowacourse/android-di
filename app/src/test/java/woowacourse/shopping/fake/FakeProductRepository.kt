package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.CHICKEN_BREAST
import woowacourse.shopping.fixture.EGG
import woowacourse.shopping.fixture.POTATO
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    private val products: List<Product> =
        listOf(
            POTATO,
            CHICKEN_BREAST,
            EGG,
        )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
