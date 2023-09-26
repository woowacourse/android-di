package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.RoomDB
import woowacourse.shopping.di.container.InstanceContainer

class DefaultApplicationModule(
    applicationContext: Context,
    instanceContainer: InstanceContainer,
) : ApplicationModule(applicationContext, instanceContainer) {
    @RoomDB
    fun provideRoomCartRepository(cartProductDao: CartProductDao): CartRepository =
        RoomCartRepository(cartProductDao)

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun getCartProductDao(): CartProductDao =
        ShoppingDatabase.getShoppingDatabase(applicationContext).cartProductDao()
}
