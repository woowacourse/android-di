package woowacourse.shopping.data.repository

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

@Singleton
class InMemoryCartRepository
    @Inject
    constructor() : CartRepository {
        private val cartProducts: MutableList<Product> = mutableListOf()

        override suspend fun addCartProduct(product: Product) {
            cartProducts.add(product)
        }

        override suspend fun getAllCartProducts(): List<Product> = cartProducts.toList()

        override suspend fun deleteCartProduct(id: Long) {
            cartProducts.removeIf { product -> product.id == id }
        }
    }
