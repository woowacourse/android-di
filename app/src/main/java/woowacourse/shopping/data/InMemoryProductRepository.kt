package woowacourse.shopping.data

import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.model.Product

@InMemory
class InMemoryProductRepository : ProductRepository {
    @Inject
    private val products: List<Product> = emptyList()

    override fun getAllProducts(): List<Product> {
        return products
    }
}
