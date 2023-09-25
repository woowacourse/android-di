package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.Module
import woowacourse.shopping.annotation.ApplicationContext
import woowacourse.shopping.annotation.Database
import woowacourse.shopping.annotation.InMemory
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository

class ApplicationModule(private val context: Context) : Module {

    @ApplicationContext
    fun provideContext(): Context = context

    fun provideCartDao(): CartProductDao = ShoppingDatabase.getInstance(context).cartProductDao()

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @Database
    fun provideDatabaseCartRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = DatabaseCartRepository(cartProductDao)
}
