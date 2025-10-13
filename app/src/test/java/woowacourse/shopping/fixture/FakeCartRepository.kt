package woowacourse.shopping.fixture

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toPresentation
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.Product
import woowacourse.shopping.ui.model.CartUiModel

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<CartUiModel> = mutableListOf()

    fun setCartProducts(products: List<CartUiModel>) {
        cartProducts.clear()
        cartProducts.addAll(products)
    }

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity().toPresentation())
    }

    override suspend fun getAllCartProducts(): List<CartUiModel> = cartProducts.toList()

    override suspend fun deleteCartProduct(id: Long) {
        if (id in cartProducts.indices) {
            cartProducts.removeAt(id.toInt())
        }
    }
}
