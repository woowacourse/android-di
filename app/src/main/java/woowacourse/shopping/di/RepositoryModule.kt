package woowacourse.shopping.di

import com.example.di.MyInjector
import com.example.di.Qualifier
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@Qualifier
annotation class Test

@Qualifier
annotation class Production

object RepositoryModule {
    @Production
    fun provideCartRepository(
        @MyInjector impl: DatabaseCartRepository,
    ): CartRepository = impl

    @Test
    fun provideCartRepository(
        @MyInjector impl: InMemoryCartRepository,
    ): CartRepository = impl

    fun provideProductRepository(
        @MyInjector impl: InMemoryProductRepository,
    ): ProductRepository = impl
}
