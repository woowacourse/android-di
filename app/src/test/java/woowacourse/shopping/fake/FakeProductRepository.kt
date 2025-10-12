package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.CHICKEN_BREAST_PRODUCT
import woowacourse.shopping.fixture.EGG_PRODUCT
import woowacourse.shopping.fixture.POTATO_PRODUCT
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    private val products: List<Product> =
        listOf(
            POTATO_PRODUCT,
            CHICKEN_BREAST_PRODUCT,
            EGG_PRODUCT,
        )

    override fun getAllProducts(): List<Product> {
        return products
    }
}
