package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import olive.di.DIContainer
import olive.di.DIModule
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.module.DaoDIModule
import woowacourse.shopping.module.RepositoryDIModule

class ShoppingApplication : Application(), DIModule {
    val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database",
        ).build()
    }
    private val diModules = listOf(DaoDIModule::class, RepositoryDIModule::class)
    val diContainer: DIContainer by lazy {
        DIContainer(
            this,
            ShoppingApplication::class,
            diModules,
        )
    }
}
