package woowacourse.shopping.data.di

import com.woowacourse.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
class DaoModule {
    fun provideCartProductDao(shoppingDatabase: ShoppingDatabase): CartProductDao = shoppingDatabase.cartProductDao()

    fun provideCartProductMemory(): CartProductDao = InMemoryCartProductDao()
}
