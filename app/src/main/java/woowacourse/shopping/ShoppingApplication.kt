package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.annotation.InMemoryCart
import woowacourse.shopping.data.annotation.RoomCart
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db =
            Room
                .databaseBuilder<ShoppingDatabase>(this, "shopping-database")
                .setDriver(AndroidSQLiteDriver())
                .build()

        DIContainer.bindInstance(
            type = CartProductDao::class,
            instance = db.cartProductDao(),
        )
        DIContainer.bind(
            type = CartRepository::class,
            implementation = DefaultCartRepository::class,
            qualifier = RoomCart::class,
        )
        DIContainer.bind(
            type = CartRepository::class,
            implementation = InMemoryCartRepository::class,
            qualifier = InMemoryCart::class,
        )
    }
}
