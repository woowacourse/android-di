package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.HunnitFactory
import woowacourse.shopping.di.InMemoryCart
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(this, ShoppingDatabase::class.java, "shopping.db").build()

        HunnitFactory.register(Context::class, applicationContext)

        HunnitFactory.register(CartProductDao::class, database.cartProductDao())
        HunnitFactory.bind(CartRepository::class, DefaultCartRepository::class, RoomCart::class)
        HunnitFactory.bind(CartRepository::class, InMemoryCartRepository::class, InMemoryCart::class)

        HunnitFactory.bind(
            ProductRepository::class,
            ProductRepository::class,
            scope = ShoppingScopes.VIEW_MODEL,
        )

        HunnitFactory.bind(
            DateFormatter::class,
            DateFormatter::class,
            scope = ShoppingScopes.SCREEN,
        )
    }
}
