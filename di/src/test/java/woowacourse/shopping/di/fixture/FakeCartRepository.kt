package woowacourse.shopping.di.fixture

import woowacourse.shopping.di.InMemory
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

@InMemory
class FakeCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) = Unit

    override suspend fun getAllCartProducts(): List<Product> = emptyList()

    override suspend fun deleteCartProduct(id: Long) = Unit
}
