package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.woowacourse.di.DIContainer
import com.woowacourse.di.DIModule
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.module.DaoDIModule
import woowacourse.shopping.module.RepositoryDIModule

class ShoppingApplication : Application(), DIModule {
    val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }
    private val diModules = listOf(this::class, DaoDIModule::class, RepositoryDIModule::class)
    val diContainer: DIContainer by lazy { DIContainer(diModules) }

    fun bindApplication(): ShoppingApplication = this
}
