package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.domain.CartRepository
import javax.inject.Inject

class InMemoryCartRepository
    @Inject
    constructor() : CartRepository {
        private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

        override suspend fun addCartProduct(product: CartProductEntity) {
            cartProducts.add(product)
        }

        override suspend fun getAllCartProducts(): List<CartProductEntity> = cartProducts.toList()

        override suspend fun deleteCartProduct(id: Long) {
            cartProducts.removeAt(id.toInt())
        }
    }
