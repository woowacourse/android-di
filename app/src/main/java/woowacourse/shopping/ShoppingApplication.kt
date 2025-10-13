package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.InjectorViewModelFactory

class ShoppingApplication : Application() {
    private val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            this,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }

    val appContainer by lazy { AppContainer().apply { registerSingleton(shoppingDatabase) } }
    val dependencyInjector by lazy { DependencyInjector(appContainer) }
    val injectorViewModelFactory by lazy { InjectorViewModelFactory(dependencyInjector) }
}
