package woowacourse.shopping.hilt.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.hilt.HiltInMemory
import woowacourse.shopping.hilt.HiltRoomDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {
    @Binds
    @Singleton
    @HiltRoomDB
    abstract fun bindsDefaultCartRepository(impl: DefaultCartRepository): CartRepository

    @Binds
    @Singleton
    @HiltInMemory
    abstract fun inMemoryCartRepository(impl: InMemoryCartRepository): CartRepository
}
