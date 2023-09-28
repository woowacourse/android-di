package woowacourse.shopping.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.cart.DefaultCartRepository
import woowacourse.shopping.data.cart.InMemoryCartRepository
import woowacourse.shopping.di.qualifire.DataBase
import woowacourse.shopping.di.qualifire.InMemory
import woowacourse.shopping.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartRepositoryModule {

    @DataBase
    @Singleton
    @Binds
    abstract fun bindDatabaseCartRepository(repository: DefaultCartRepository): CartRepository

    @Binds
    @InMemory
    @Singleton
    abstract fun bindInMemoryCartRepository(repository: InMemoryCartRepository): CartRepository
}
