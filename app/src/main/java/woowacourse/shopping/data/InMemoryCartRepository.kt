package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val carts: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        carts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = carts.map { it.toModel() }

    override suspend fun deleteCartProduct(id: Long) {
        carts.removeAt(0)
    }
}
