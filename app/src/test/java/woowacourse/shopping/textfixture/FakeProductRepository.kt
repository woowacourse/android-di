package woowacourse.shopping.textfixture

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.ProductRepository

class FakeProductRepository(
    private val products: List<Product>,
) : ProductRepository {
    override fun getAllProducts(): List<Product> = products
}
