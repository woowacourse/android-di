package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val storage = mutableListOf<Product>()

    override suspend fun addCartProduct(product: Product) {
        storage.add(product)
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> = storage.map { it.toTestEntity() }.toList()

    override suspend fun deleteCartProduct(id: Long) {
        storage.removeIf { it.id == id }
    }
}

private fun Product.toTestEntity(): CartProductEntity {
    val entity =
        CartProductEntity(
            name = name,
            price = price,
            imageUrl = imageUrl,
        )
    entity.id = this.id
    return entity
}
