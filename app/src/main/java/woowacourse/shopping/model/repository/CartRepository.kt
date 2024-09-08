package woowacourse.shopping.model.repository

import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.injection.repository.RepositoryDI

interface CartRepository : RepositoryDI {
    fun addCartProduct(product: Product)

    fun getAllCartProducts(): List<Product>

    fun deleteCartProduct(id: Int)
}
