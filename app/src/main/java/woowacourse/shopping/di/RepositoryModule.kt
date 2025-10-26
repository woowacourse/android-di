package woowacourse.shopping.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    @RoomDBCartRepository
    abstract fun bindCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository
}
