package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DependencyContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        val cartProductDao = database.cartProductDao()
        DependencyContainer.register(Context::class, applicationContext)
        DependencyContainer.register(CartProductDao::class, cartProductDao)
        DependencyContainer.register(
            CartRepository::class,
            RoomCart::class,
            RoomCartRepository(cartProductDao),
        )
        DependencyContainer.register(
            CartRepository::class,
            InMemoryCart::class,
            InMemoryCartRepository(),
        )
    }
}
