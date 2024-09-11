package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartProduct(cartProducts.size.toLong()))
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
