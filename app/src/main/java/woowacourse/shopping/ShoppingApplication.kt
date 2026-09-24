package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiManager

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(applicationContext, ShoppingDatabase::class.java, "shopping.db")
                .build()
        val cartProductDao = database.cartProductDao()

        DiManager.addInstance(key = ShoppingDatabase::class.java, value = database)
        DiManager.addInstance(key = CartProductDao::class.java, value = cartProductDao)

        DiManager.addProvider(key = CartRepository::class.java, value = DefaultCartRepository::class.java)
    }
}
