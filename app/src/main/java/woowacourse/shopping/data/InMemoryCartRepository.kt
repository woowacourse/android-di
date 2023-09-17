package woowacourse.shopping.data

import woowacourse.shopping.data.di.InMemory
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@InMemory
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()
    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.map { it.toModel() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}