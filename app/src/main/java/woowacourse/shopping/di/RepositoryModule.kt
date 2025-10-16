package woowacourse.shopping.di

import woowacourse.shopping.annotation.InMemory
import woowacourse.shopping.annotation.Room
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

object RepositoryModule {
    @Room
    @Singleton
    fun provideDefaultCartRepository(dao: CartProductDao): CartRepository = DefaultCartRepository(dao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}
