package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import java.util.concurrent.atomic.AtomicLong

class FakeCartRepository : CartRepository {
    private val storage = mutableListOf<CartProduct>()
    private var id: AtomicLong = AtomicLong(1L)

    override suspend fun addCartProduct(product: Product) {
        val insertId = id.getAndIncrement()
        storage.add(product.toTestCartProduct(insertId, System.currentTimeMillis()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = storage.toList()

    override suspend fun deleteCartProduct(id: Long) {
        storage.removeIf { it.id == id }
    }
}

private fun Product.toTestCartProduct(
    id: Long,
    createdAt: Long,
) = CartProduct(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl,
    createdAt = createdAt,
)
