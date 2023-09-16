package woowacourse.shopping

import android.app.Application
import woowacourse.di.Container
import woowacourse.di.Injector
import woowacourse.shopping.di.AppModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injector = Injector(Container(AppModule(applicationContext)))
    }

    companion object {
        lateinit var injector: Injector
    }
}
