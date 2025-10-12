package woowacourse.shopping.data.repository

import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.shopping.data.db.DatabaseModule
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule(
    private val databaseModule: DatabaseModule,
) : DependencyModule {
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }

    @Qualifier("default")
    val defaultCartRepository: CartRepository by lazy { DefaultCartRepository(databaseModule.cartDao) }

    @Qualifier("inMemory")
    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }
}
