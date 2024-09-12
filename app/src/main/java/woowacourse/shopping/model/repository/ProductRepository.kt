package woowacourse.shopping.model.repository

import woowacourse.shopping.di.repository.RepositoryDI
import woowacourse.shopping.model.Product

interface ProductRepository : RepositoryDI {
    fun getAllProducts(): List<Product>
}
