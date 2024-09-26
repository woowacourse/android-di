package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import olive.di.DIContainer
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.module.DaoDIModule
import woowacourse.shopping.module.DateFormatterDIModule
import woowacourse.shopping.module.RepositoryDIModule

class ShoppingApplication : Application() {
    val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database",
        ).build()
    }
    private val diModules = listOf(DaoDIModule::class, RepositoryDIModule::class, DateFormatterDIModule::class)
    val diContainer: DIContainer by lazy {
        DIContainer(
            this,
            ShoppingApplication::class,
            diModules,
        )
    }
}
