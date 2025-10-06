package woowacourse.shopping.fake

import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.GOBCHANG
import woowacourse.shopping.fixture.MALATANG
import woowacourse.shopping.fixture.TONKATSU
import woowacourse.shopping.model.Product

class FakeProductRepository : ProductRepository {
    override fun getAllProducts(): List<Product> {
        return listOf(
            TONKATSU,
            MALATANG,
            GOBCHANG,
        )
    }
}
