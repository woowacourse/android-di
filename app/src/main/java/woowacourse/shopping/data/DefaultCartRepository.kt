package woowacourse.shopping.data

import woowacourse.shopping.data.dataSorce.LocalDataSource
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val localDataSource: LocalDataSource,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        localDataSource.insert(product)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return localDataSource.getAll()
    }

    override suspend fun deleteCartProduct(id: Long) {
        localDataSource.delete(id)
    }
}
