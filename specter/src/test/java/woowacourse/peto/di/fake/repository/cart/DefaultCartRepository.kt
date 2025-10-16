package woowacourse.peto.di.fake.repository.cart

import woowacourse.peto.di.fake.model.CartProduct
import woowacourse.peto.di.fake.model.Product

class DefaultCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) {}

    override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

    override suspend fun deleteCartProduct(id: Long) {}
}
