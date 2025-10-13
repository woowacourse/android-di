package woowacourse.shopping.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Provides
import com.example.di.annotation.Qualifier
import com.example.di.annotation.Singleton
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

object RepositoryModule {
    @Provides
    @Singleton
    @Database
    fun provideCartRepository(
        @Inject impl: DatabaseCartRepository,
    ): CartRepository = impl

    @Provides
    @Singleton
    @InMemory
    fun provideCartRepository(
        @Inject impl: InMemoryCartRepository,
    ): CartRepository = impl

    @Provides
    @Singleton
    fun provideProductRepository(
        @Inject impl: InMemoryProductRepository,
    ): ProductRepository = impl
}
