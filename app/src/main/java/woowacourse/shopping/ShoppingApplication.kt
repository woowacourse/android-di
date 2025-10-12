package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.InjectContainer
import woowacourse.shopping.di.module.DatabaseModule
import woowacourse.shopping.di.module.RepositoryModule
import woowacourse.shopping.di.module.ViewModelModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        InjectContainer.init(
            modules =
                listOf(
                    RepositoryModule(),
                    DatabaseModule(applicationContext),
                    ViewModelModule(),
                ),
        )
    }
}
