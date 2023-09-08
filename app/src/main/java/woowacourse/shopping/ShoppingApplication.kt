package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(listOf(AppModule(applicationContext)))
    }

    companion object {
        lateinit var injector: Injector
    }
}
