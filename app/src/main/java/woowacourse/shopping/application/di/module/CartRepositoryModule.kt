package woowacourse.shopping.application.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.application.di.annotaion.Database
import woowacourse.shopping.application.di.annotaion.InMemory
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {
    @Binds
    @Database
    @Singleton
    abstract fun provideDatabaseCartRepository(databaseCartRepository: DatabaseCartRepository): CartRepository

    @Binds
    @InMemory
    @Singleton
    abstract fun provideInMemoryCartRepository(inMemoryCartRepository: InMemoryCartRepository): CartRepository
}
