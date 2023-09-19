package woowacourse.shopping.di

import woowacourse.shopping.data.entity.CartProductDao
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.di.annotation.Database
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.repository.CartRepository

object RepositoryModule {

    fun provideProductRepository(): DefaultProductRepository =
        DefaultProductRepository()

    @InMemory
    fun provideCartRepository(): CartRepository =
        InMemoryCartRepository()

    @Database
    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository =
        DatabaseCartRepository(cartProductDao)
}
