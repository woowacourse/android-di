package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

@MyModule
object RepositoryModule {
    @MyProvider
    fun cartRepository(dao: CartProductDao): DefaultCartRepository = DefaultCartRepository(dao)

    @MyProvider
    fun productRepository(): DefaultProductRepository = DefaultProductRepository()

    @MyProvider
    fun cartProductDao(): CartProductDao = LocalStorageModule.cartProductDao
}
