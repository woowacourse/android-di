package woowacourse.shopping.ui

import android.app.Application
import com.kmlibs.supplin.Injector
import woowacourse.shopping.di.CartModule
import woowacourse.shopping.di.DatabaseModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        Injector.init {
            applicationModule(this@ShoppingApplication, DatabaseModule::class, CartModule::class)
        }
    }
}
