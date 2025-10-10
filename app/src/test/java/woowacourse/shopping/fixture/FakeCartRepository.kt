package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val storage = mutableListOf<Product>()

    override suspend fun addCartProduct(product: Product) {
        storage.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> = storage.map { it.toEntity() }.toList()

    override suspend fun deleteCartProduct(id: Long) {
        storage.removeAt(id.toInt())
    }
}
