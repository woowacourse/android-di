package woowacourse.shopping.model.repository

import woowacourse.shopping.di.repository.RepositoryDI
import woowacourse.shopping.model.Product

interface CartRepository : RepositoryDI {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Int)
}
