package woowacourse.shopping

import RepositoryModule
import android.app.Application
import com.yrsel.di.DependencyContainer
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.UtilModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.init(
            application = this,
            modules =
                arrayOf(
                    RepositoryModule(),
                    DatabaseModule(applicationContext),
                    UtilModule(),
                ),
        )
    }
}
