package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.DefaultAppModule
import woowacourse.shopping.di.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        Injector.init {
            context(this@ShoppingApplication)
            module(DefaultAppModule::class)
        }
    }
}
