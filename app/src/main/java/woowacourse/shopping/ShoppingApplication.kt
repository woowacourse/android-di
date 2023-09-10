package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.Module

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
