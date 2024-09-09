package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DependencyInjector

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.loadModule(RepositoryModule())
    }
}
