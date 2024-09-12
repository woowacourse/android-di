package woowacourse.shopping.model.repository

import woowacourse.shopping.model.Product
import woowacourse.shopping.di.repository.RepositoryDI

interface ProductRepository : RepositoryDI {
    fun getAllProducts(): List<Product>
}
