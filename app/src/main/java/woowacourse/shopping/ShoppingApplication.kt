package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.module.RepositoryModule
import woowacourse.shopping.otterdi.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        injector = Injector(RepositoryModule(this))
    }

    companion object {
        lateinit var injector: Injector
    }
}
