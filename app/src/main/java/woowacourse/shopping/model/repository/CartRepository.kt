package woowacourse.shopping.model.repository

import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.injection.repository.RepositoryDI

interface CartRepository : RepositoryDI {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Int)
}
