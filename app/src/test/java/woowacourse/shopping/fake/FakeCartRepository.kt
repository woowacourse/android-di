package woowacourse.shopping.fake

import woowacourse.shopping.data.mapper.toCartEntity
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.POTATO_CART_PRODUCT
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<CartProduct> = mutableListOf(POTATO_CART_PRODUCT)

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toCartEntity().toCartProduct())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAll { it.id == id }
    }
}
