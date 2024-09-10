package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

interface AppModule : InjectionModule {
    fun provideShoppingDatabase(): ShoppingDatabase

    fun provideProductRepository(): ProductRepository

    fun provideCartRepository(shoppingDatabase: ShoppingDatabase): CartRepository
}
