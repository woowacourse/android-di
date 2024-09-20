package woowacourse.shopping

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

object RepositoryModule {
    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository = CartDefaultRepository(cartProductDao)

    fun provideCartInMemoryRepository(): CartRepository = CartInMemoryRepository()
}
