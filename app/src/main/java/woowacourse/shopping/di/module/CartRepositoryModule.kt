package woowacourse.shopping.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository
}
