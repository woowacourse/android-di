package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class RepositoryModule {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
