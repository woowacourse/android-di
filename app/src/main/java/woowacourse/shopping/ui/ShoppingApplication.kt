package woowacourse.shopping.ui

import android.app.Application
import com.kmlibs.supplin.Injector
import woowacourse.shopping.di.DefaultAppModule

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
