package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProductEntity>()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.map { it.toModel() }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { id == it.id }
    }
}
