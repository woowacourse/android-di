package woowacourse.shopping.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartProductRepositoryModule {

    @Singleton
    @Database
    @Binds
    abstract fun bindDatabaseCartProductRepository(
        databaseCartRepository: DatabaseCartRepository
    ): CartRepository

    @Singleton
    @InMemory
    @Binds
    abstract fun bindInMemoryCartProductRepository(
        inMemoryCartRepository: InMemoryCartRepository
    ): CartRepository
}
