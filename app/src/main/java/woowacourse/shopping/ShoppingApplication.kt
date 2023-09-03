package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(RepositoryModule)
    }

    companion object {
        lateinit var injector: Injector
    }
}
