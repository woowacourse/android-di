package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.RepositoryModule

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.loadModule(RepositoryModule())
    }
}
