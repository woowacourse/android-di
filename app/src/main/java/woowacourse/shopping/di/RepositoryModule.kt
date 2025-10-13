package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

@MyModule
object RepositoryModule {
    fun cartRepository(dao: CartProductDao): DefaultCartRepository = DefaultCartRepository(dao)

    fun productRepository(): DefaultProductRepository = DefaultProductRepository()

    fun cartProductDao(): CartProductDao = LocalStorageModule.cartProductDao
}
