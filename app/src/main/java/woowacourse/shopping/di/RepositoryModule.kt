package woowacourse.shopping.di

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryModule {
    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()
    fun provideCartRepository(): CartRepository = CartDefaultRepository()
}
