package woowacourse.shopping.data.repository

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.data.db.DatabaseProvider
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule(
    private val databaseProvider: DatabaseProvider,
) : DependencyModule {
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    val cartRepository: CartRepository by lazy { DefaultCartRepository(databaseProvider.cartDao) }
}
