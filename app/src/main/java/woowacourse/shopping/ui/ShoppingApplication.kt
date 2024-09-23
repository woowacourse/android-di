package woowacourse.shopping.ui

import android.app.Application
import com.kmlibs.supplin.Injector
import woowacourse.shopping.di.CartModule
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.ProductModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        Injector.init {
            context(this@ShoppingApplication)
            module(DatabaseModule::class, ProductModule::class, CartModule::class)
        }
    }
}
