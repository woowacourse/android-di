package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> =
        cartProducts.map { it.toCartProduct() }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
