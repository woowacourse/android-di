package woowacourse.shopping.di.repositorymodule

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.Default
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.model.CartRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonRepositoryModule {
    @Default
    @Binds
    @Singleton
    abstract fun defaultCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository

    @InMemory
    @Binds
    @Singleton
    abstract fun inMemoryCartRepository(inMemoryCartRepository: InMemoryCartRepository): CartRepository
}
