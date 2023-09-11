package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.SingletonModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        injector = Injector(
            SingletonModule.getSingletonModule(context = applicationContext),
        )
    }

    companion object {
        lateinit var injector: Injector
    }
}
