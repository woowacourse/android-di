package woowacourse.shopping.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {
    @Binds
    @Singleton
    @RoomDatabase
    abstract fun bindRoomCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository

    @Binds
    @Singleton
    @InMemory
    abstract fun bindInMemoryCartRepository(inMemoryCartRepository: InMemoryCartRepository): CartRepository
}
