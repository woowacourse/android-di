package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.RoomDB

class DefaultApplicationModule(
    applicationContext: Context,
) : ApplicationModule(applicationContext) {
    @RoomDB
    fun provideRoomCartRepository(cartProductDao: CartProductDao): CartRepository =
        RoomCartRepository(cartProductDao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun getCartProductDao(): CartProductDao =
        ShoppingDatabase.getShoppingDatabase(applicationContext).cartProductDao()
}
