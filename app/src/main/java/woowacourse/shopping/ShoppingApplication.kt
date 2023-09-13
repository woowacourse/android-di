package woowacourse.shopping

import android.app.Application
import com.now.di.Injector
import woowacourse.shopping.di.module.DefaultModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        injectModule()
    }

    private fun injectModule() {
        Injector.addModule(DefaultModule(applicationContext))
    }
}
