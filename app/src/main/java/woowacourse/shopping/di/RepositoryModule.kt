package woowacourse.shopping.di

import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.RoomDB
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

object RepositoryModule {
    @RoomDB
    @Singleton
    fun provideDefaultCartRepository(dao: CartProductDao): CartRepository = DefaultCartRepository(dao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()
}
