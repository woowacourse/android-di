package woowacourse.shopping.ui

import android.app.Application
import com.kmlibs.supplin.Injector
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.InMemoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        Injector.init {
            context(this@ShoppingApplication)
            module(DatabaseModule::class, InMemoryModule::class)
        }
    }
}
