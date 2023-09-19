package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class DefaultViewModelModule(
    context: Context,
) : ViewModelModule(context) {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    fun provideRoomCartRepository(cartProductDao: CartProductDao): CartRepository =
        RoomCartRepository(cartProductDao)

    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    fun getCartProductDao(): CartProductDao =
        ShoppingDatabase.getShoppingDatabase(applicationContext).cartProductDao()
}
