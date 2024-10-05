package woowacourse.shopping.hilt.fake

import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.model.Product
import javax.inject.Inject

class StubCartRepository @Inject constructor() : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        println("addCartProduct")
    }

    override suspend fun allCartProducts(): List<Product> {
        return emptyList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        println("deleteCartProduct")
    }
}