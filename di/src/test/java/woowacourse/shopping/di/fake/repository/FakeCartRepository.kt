package woowacourse.shopping.di.fake.repository

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.model.Product

class FakeCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) {}

    override suspend fun getAllCartProducts(): List<Product> = emptyList()

    override suspend fun deleteCartProduct(id: Long) {}
}
