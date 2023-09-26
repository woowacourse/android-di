package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.di.qualifier.RoomDB
import woowacourse.shopping.hasydi.Module
import woowacourse.shopping.hasydi.annotation.ApplicationContext
import woowacourse.shopping.hasydi.annotation.Inject
import woowacourse.shopping.hasydi.annotation.Singleton
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class AppModule : Module {

    override var context: Context? = null

    fun provideProductRepository(): ProductRepository = ProductDefaultRepository()

    @Singleton
    @RoomDB
    fun provideCartDefaultRepository(
        @Inject cartProductDao: CartProductDao,
    ): CartRepository = CartDefaultRepository(cartProductDao)

    @Singleton
    @InMemory
    fun provideCartInMemoryRepository(): CartRepository = CartInMemoryRepository()

    @Singleton
    fun provideCartProductDao(
        @ApplicationContext @Inject
        context: Context,
    ): CartProductDao = ShoppingDatabase.getInstance(context).cartProductDao()

    @ApplicationContext
    fun provideContext(): Context = context!!
}
