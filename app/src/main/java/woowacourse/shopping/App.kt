package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.RepositoryModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppContainer.provideModule(RepositoryModule::class)
    }
}
