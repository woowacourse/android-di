package woowacourse.shopping.data

import woowacourse.shopping.data.datasource.CartDataSource
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class DefaultCartRepository(
    private val cartDataSource: CartDataSource,
) : CartRepository {

    override suspend fun addCartProduct(product: Product): Long {
        return cartDataSource.addCartProduct(product)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartDataSource.getAllCartProducts()
    }

    override suspend fun deleteCartProduct(id: Long) {
        return cartDataSource.deleteCartProduct(id)
    }
}
