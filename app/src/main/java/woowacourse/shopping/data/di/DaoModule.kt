package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DaoModule {
    fun provideCartProductDao(shoppingDatabase: ShoppingDatabase): CartProductDao = shoppingDatabase.cartProductDao()

    fun provideCartProductMemory(): CartProductDao = InMemoryCartProductDao()
}
