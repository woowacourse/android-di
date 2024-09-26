package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import olive.di.DIContainer
import olive.di.DIModule
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.module.DaoDIModule
import woowacourse.shopping.module.DateFormatterDIModule
import woowacourse.shopping.module.RepositoryDIModule
import kotlin.reflect.KClass

class ShoppingApplication : Application() {
    val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database",
        ).build()
    }
    private val diModules: List<KClass<out DIModule>> by lazy {
        listOf(DaoDIModule::class, RepositoryDIModule::class, DateFormatterDIModule::class)
    }

    override fun onCreate() {
        super.onCreate()
        DIContainer.injectApplication(this, ShoppingApplication::class)
        DIContainer.injectModules(diModules)
    }
}
