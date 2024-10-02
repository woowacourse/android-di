package woowacourse.shopping.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun productRepository(
        productRepository: InMemoryProductRepository
    ): ProductRepository

    @Binds
    abstract fun cartRepository(
        cartRepository: InMemoryCartRepository
    ): CartRepository
}