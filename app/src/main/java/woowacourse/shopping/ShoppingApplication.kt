package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AoDi

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

        AoDi.register(CartProductDao::class, database.cartProductDao())
        AoDi.registerInterfaceRule(CartRepository::class, DefaultCartRepository::class)
    }
}
