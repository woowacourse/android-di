package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.example.di.di.AppContainer
import com.example.di.di.DependencyInjector
import com.example.di.di.InjectorViewModelFactory
import woowacourse.shopping.data.ShoppingDatabase

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
