package woowacourse.shopping

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.di.RoomDB
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

object RepositoryModule {
    @InMemory
    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    @RoomDB
    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository = CartDefaultRepository(cartProductDao)

    @InMemory
    fun provideCartInMemoryRepository(): CartRepository = CartInMemoryRepository()
}
