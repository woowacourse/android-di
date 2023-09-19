package woowacourse.shopping

import android.app.Application
import woowacourse.di.Container
import woowacourse.di.Injector
import woowacourse.shopping.di.AppModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = AppModule().apply { setModuleContext(applicationContext) }
        val container = Container(module)
        injector = Injector(container)
    }

    companion object {
        lateinit var injector: Injector
    }
}
