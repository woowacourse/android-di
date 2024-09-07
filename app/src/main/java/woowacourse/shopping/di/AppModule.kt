package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

interface AppModule : InjectionModule {
    fun provideProductRepository(): ProductRepository

    fun provideCartRepository(): CartRepository
}
