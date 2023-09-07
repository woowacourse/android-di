package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.di.module.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        injector = Injector(RepositoryModule)
    }

    companion object {
        lateinit var injector: Injector
    }
}
