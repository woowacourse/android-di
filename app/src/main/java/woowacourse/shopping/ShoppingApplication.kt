package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.HunnitFactory
import woowacourse.shopping.di.InMemoryCart
import woowacourse.shopping.di.RoomCart

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(this, ShoppingDatabase::class.java, "shopping.db").build()
        HunnitFactory.register(CartProductDao::class, database.cartProductDao())
        HunnitFactory.bind(CartRepository::class, DefaultCartRepository::class, RoomCart::class)
        HunnitFactory.bind(CartRepository::class, InMemoryCartRepository::class, InMemoryCart::class)
    }
}
