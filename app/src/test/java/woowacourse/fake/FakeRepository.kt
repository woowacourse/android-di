package woowacourse.fake

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.model.Product

interface FakeRepository {
    fun getValue(): String
}

class NotRegisteredRepository : FakeRepository {
    override fun getValue(): String = "NotRegistered"
}

class FakeCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) {}

    override suspend fun getAllCartProducts(): List<Product> = emptyList()

    override suspend fun deleteCartProduct(id: Long) {}
}
