package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppClassLoader
import woowacourse.shopping.di.ClassLoader
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DIModule
import woowacourse.shopping.di.Module

class ShoppingApplication : Application(), DIModule {
    private val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }
    private val classLoader: ClassLoader by lazy { AppClassLoader(this) }
    val diContainer: DIContainer by lazy { DIContainer(this, classLoader) }

    override fun singletonInstance(): List<Module> {
        return listOf(Module(CartProductDao::class, shoppingDatabase.cartProductDao()))
    }
}
