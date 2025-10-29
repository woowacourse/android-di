package woowacourse.shopping.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Default
    @Binds
    @Singleton
    abstract fun defaultCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository

    @InMemory
    @Binds
    @Singleton
    abstract fun inMemoryCartRepository(inMemoryCartRepository: InMemoryCartRepository): CartRepository

    @Binds
    @Singleton
    abstract fun productRepository(defaultProductRepository: DefaultProductRepository): ProductRepository
}
