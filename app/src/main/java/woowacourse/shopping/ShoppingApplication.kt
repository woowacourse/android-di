package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(applicationContext, ShoppingDatabase::class.java, "shopping.db")
                .build()
        val cartProductDao = database.cartProductDao()

        DiContainer.addInstance(key = ShoppingDatabase::class.java, value = database)
        DiContainer.addInstance(key = CartProductDao::class.java, value = cartProductDao)

        DiContainer.addProvider(key = CartRepository::class.java, value = DefaultCartRepository::class.java)
    }
}
