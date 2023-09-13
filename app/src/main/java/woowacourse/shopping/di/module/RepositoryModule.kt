package woowacourse.shopping.di.module

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.cart.DefaultCartRepository
import woowacourse.shopping.data.cart.InMemoryCartRepository
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryModule : Module {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    @Qualifier("DefaultCartRepository")
    fun provideDatabaseCartRepository(): CartRepository =
        DefaultCartRepository(ShoppingApplication.cartProductDao)

    @Qualifier("InMemoryCartRepository")
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }
}
