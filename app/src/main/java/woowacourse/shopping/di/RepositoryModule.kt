package woowacourse.shopping.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Default
    @Provides
    @Singleton
    fun defaultCartRepository(dao: CartProductDao): CartRepository = DefaultCartRepository(dao)

    @InMemory
    @Provides
    @Singleton
    fun inMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @Provides
    @Singleton
    fun productRepository(): ProductRepository = DefaultProductRepository()
}
