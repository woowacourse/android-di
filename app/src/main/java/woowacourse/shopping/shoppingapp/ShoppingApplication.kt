package woowacourse.shopping.shoppingapp

import android.app.Application
import woowacourse.shopping.data.di.DaoModule
import woowacourse.shopping.data.di.DatabaseModule
import woowacourse.shopping.data.di.RepositoryModule
import woowacourse.shopping.shoppingapp.di.AppModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.setInstance(
            context = applicationContext,
            modules =
                listOf(
                    DatabaseModule::class,
                    DaoModule::class,
                    RepositoryModule::class,
                ),
        )
    }
}
