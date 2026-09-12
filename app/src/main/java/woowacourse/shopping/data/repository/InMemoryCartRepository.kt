package woowacourse.shopping.data.repository

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Inject

class InMemoryCartRepository
    @Inject
    constructor() : CartRepository {
        private val cartProducts: MutableList<CartProduct> = mutableListOf()

        override suspend fun addCartProduct(cartProduct: CartProduct) {
            cartProducts.add(cartProduct)
        }

        override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.toList()

        override suspend fun deleteCartProduct(id: Long) {
            cartProducts.removeIf { product -> product.id == id }
        }
    }
