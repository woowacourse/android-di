package woowacourse.shopping.data.repository

import woowacourse.shopping.data.db.DatabaseProvider
import woowacourse.shopping.di.DependencyModule
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule(
    private val databaseProvider: DatabaseProvider,
) : DependencyModule {
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }

    @Qualifier("default")
    val defaultCartRepository: CartRepository by lazy { DefaultCartRepository(databaseProvider.cartDao) }

    @Qualifier("inMemory")
    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }
}
