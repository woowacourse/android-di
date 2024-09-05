package woowacourse.shopping.model.repository

import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.injection.RepositoryDI

interface ProductRepository : RepositoryDI {
    fun getAllProducts(): List<Product>
}
