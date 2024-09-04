package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

object RepositoryModule {
    fun provideProductRepository(): ProductRepository = ProductRepository()

    fun provideCartRepository(): CartRepository = CartRepository()
}
