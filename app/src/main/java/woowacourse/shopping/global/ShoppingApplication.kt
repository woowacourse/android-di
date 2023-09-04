package woowacourse.shopping.global

import android.app.Application
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.module.NormalModule
import woowacourse.shopping.di.module.SingletonModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = AutoInjector(listOf(SingletonModule(), NormalModule()))
    }

    companion object {
        lateinit var injector: Injector
            private set
    }
}
