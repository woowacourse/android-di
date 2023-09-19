package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.module.RepositoryModule
import woowacourse.shopping.otterdi.Injector
import woowacourse.shopping.otterdi.Module

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        module = RepositoryModule(this)
        injector = Injector(module)
    }

    companion object {
        lateinit var module: Module
        lateinit var injector: Injector
    }
}
