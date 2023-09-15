package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.hashdi.AppContainer
import woowacourse.shopping.hashdi.Injector
import woowacourse.shopping.hashdi.Module

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules: List<Module> = listOf(AppModule(applicationContext))
        injector = Injector(AppContainer(modules))
    }

    companion object {
        lateinit var injector: Injector
    }
}
