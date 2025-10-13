package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.annotation.MyModule
import woowacourse.shopping.di.annotation.MyProvider
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@MyModule
object RepositoryModule {
    @MyProvider
    fun cartRepository(dao: CartProductDao): CartRepository = DefaultCartRepository(dao)

    @MyProvider
    fun productRepository(): ProductRepository = DefaultProductRepository()
}
