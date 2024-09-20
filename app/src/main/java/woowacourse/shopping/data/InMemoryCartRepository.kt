package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import javax.inject.Singleton

@Singleton
class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartProduct(cartProducts.size.toLong()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.filterNot { it.id != id }
    }
}
