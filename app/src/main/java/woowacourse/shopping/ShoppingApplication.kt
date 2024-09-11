package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DaoDIModule
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.RepositoryDIModule

class ShoppingApplication : Application() {
    val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }
    private val diModules = listOf(RepositoryDIModule::class, DaoDIModule::class)
    val diContainer: DIContainer by lazy { DIContainer(this, diModules) }
}
