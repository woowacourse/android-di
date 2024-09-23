package woowacourse.shopping.shoppingapp

import android.app.Application
import com.woowacourse.di.DiModule
import woowacourse.shopping.data.di.DaoModule
import woowacourse.shopping.data.di.DatabaseModule
import woowacourse.shopping.data.di.RepositoryModule
import woowacourse.shopping.ui.di.DateFormatterModule
import javax.inject.Qualifier

@Qualifier
annotation class InMemoryDatabase

@Qualifier
annotation class RoomDatabase

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DiModule.setInstance(
            context = applicationContext,
            modules =
                listOf(
                    DateFormatterModule::class,
                    DatabaseModule::class,
                    DaoModule::class,
                    RepositoryModule::class,
                ),
        )
    }
}
