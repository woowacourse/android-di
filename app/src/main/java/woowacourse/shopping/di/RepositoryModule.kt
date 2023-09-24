package woowacourse.shopping.di

import com.created.customdi.annotation.Singleton
import woowacourse.shopping.data.entity.CartProductDao
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.di.annotation.Database
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryModule {

    fun provideProductRepository(): ProductRepository =
        DefaultProductRepository()

    @Singleton
    @InMemory
    fun provideCartRepository(): CartRepository =
        InMemoryCartRepository()

    @Singleton
    @Database
    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository =
        DatabaseCartRepository(cartProductDao)
}
