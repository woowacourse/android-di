package woowacourse.shopping.data.repository

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Inject

class InMemoryCartRepository
    @Inject
    constructor() : CartRepository {
        private var id: Long = 0
        private val cartProducts: MutableList<CartProduct> = mutableListOf()

        override suspend fun addCartProduct(product: Product) {
            cartProducts.add(product.toCartProduct(id++))
        }

        override suspend fun getAllCartProducts(): List<CartProduct> {
            return cartProducts.toList()
        }

        override suspend fun deleteCartProduct(id: Long) {
            cartProducts.removeAt(id.toInt())
        }
    }
